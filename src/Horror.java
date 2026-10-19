import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * The horror layer for Time Slipper.
 *
 * TimeSlipperMain calls draw(...) at the very end of paint(), so everything here is
 * painted ON TOP of the normal game. No game logic is changed.
 *
 * What it adds:
 *  - Darkness: the whole screen goes dark except a flickering light around the player
 *  - Danger vignette: red creeps in from the edges as enemies get close
 *  - Heartbeat that speeds up and gets louder when enemies are near
 *  - Low drone sound for atmosphere
 *  - Whispers: faint red text that appears and fades at random spots
 *  - A rare jumpscare using the boss image
 *
 * All sounds are generated in code, so no audio files are needed.
 * Press H in the game to turn all of this off (accessibility / debugging).
 */
public class Horror
{
	public static boolean enabled = true;

	private static final float RATE = 44100f;
	private static final String[] WHISPERS = {
		"behind you", "it can see you", "don't stop moving", "you were never here",
		"cable...", "so cold", "time is running out", "turn around", "it knows your code"
	};

	private final Random rand = new Random();
	private Clip heartClip, droneClip, scareClip;

	private boolean started = false;
	private boolean droneOn = false;
	private boolean deathStingPlayed = false;

	private long lastBeat, nextScareAt, scareUntil;
	private long nextWhisperAt, whisperStart, whisperUntil;
	private long nextDipAt, dipUntil;
	private String whisper = "";
	private int whisperX, whisperY;

	public Horror()
	{
		try
		{
			heartClip = makeClip(makeHeartbeat());
			droneClip = makeClip(makeDrone());
			scareClip = makeClip(makeScare());
			setGain(droneClip, -22f);
		}
		catch (Exception e)
		{
			// No sound device available. The visuals still work.
			System.out.println("Horror: audio unavailable (" + e.getMessage() + ")");
		}
	}

	// ------------------------------------------------------------------
	// Drawing
	// ------------------------------------------------------------------

	/**
	 * Draw the in-game horror overlay.
	 *
	 * @param px,py   player's top-left corner
	 * @param danger  0.0 (safe) to 1.0 (enemy touching you)
	 * @param scareImg image used for the jumpscare
	 * @param font    the game's font (used for whispers), can be null
	 */
	public void draw(Graphics2D g2, int w, int h, int px, int py, double danger, Image scareImg, Font font)
	{
		if (!enabled)
		{
			silence();
			return;
		}
		long now = System.currentTimeMillis();
		deathStingPlayed = false;

		if (!started)
		{
			// Start fresh timers so nothing scary fires the instant the level begins
			started = true;
			lastBeat = now;
			nextScareAt = now + 20000 + rand.nextInt(20000);
			nextWhisperAt = now + 6000 + rand.nextInt(6000);
			nextDipAt = now + 4000 + rand.nextInt(6000);
		}

		if (!droneOn && droneClip != null)
		{
			droneClip.setFramePosition(0);
			droneClip.loop(Clip.LOOP_CONTINUOUSLY);
			droneOn = true;
		}

		// ---- Jumpscare ----
		if (now >= nextScareAt && now >= scareUntil)
		{
			scareUntil = now + 450;
			nextScareAt = now + 25000 + rand.nextInt(25000);
			play(scareClip, 0f);
		}
		if (now < scareUntil && scareImg != null)
		{
			int jx = rand.nextInt(25) - 12;
			int jy = rand.nextInt(25) - 12;
			g2.setColor(Color.BLACK);                       // cut to black first
			g2.fillRect(0, 0, w, h);
			g2.drawImage(scareImg, jx - 15, jy - 15, w + 30, h + 30, null);
			g2.setPaint(new RadialGradientPaint(              // blood-red glow, dark edges
					new Point2D.Float(w / 2f, h / 2f), Math.max(w, h) * 0.7f,
					new float[] { 0f, 1f },
					new Color[] { new Color(170, 0, 0, 70), new Color(0, 0, 0, 200) }));
			g2.fillRect(0, 0, w, h);
			return;
		}

		// ---- Darkness with a flickering light around the player ----
		float cx = px + 60f;
		float cy = py + 60f;
		double radius = Math.min(w, h) * 0.24;
		radius *= 1.0 + 0.04 * Math.sin(now / 300.0);   // gentle breathing
		radius *= 1.0 - 0.25 * danger;                    // light shrinks when enemies are near

		if (now >= nextDipAt)
		{
			dipUntil = now + 180 + rand.nextInt(250);
			nextDipAt = now + 5000 + rand.nextInt(9000);
		}
		if (now < dipUntil)
		{
			radius *= 0.55;                               // a brief dim, like a failing bulb
		}

		Color clear = new Color(0, 0, 0, 0);
		g2.setPaint(new RadialGradientPaint(
				new Point2D.Float(cx, cy), (float) radius,
				new float[] { 0f, 0.35f, 1f },
				new Color[] { clear, new Color(0, 0, 0, 70), new Color(0, 0, 0, 238) }));
		g2.fillRect(0, 0, w, h);

		// ---- Red danger vignette ----
		if (danger > 0.02)
		{
			int a = (int) (Math.min(1.0, danger) * 150);
			Color redClear = new Color(120, 0, 0, 0);
			g2.setPaint(new RadialGradientPaint(
					new Point2D.Float(w / 2f, h / 2f), Math.max(w, h) * 0.75f,
					new float[] { 0f, 0.55f, 1f },
					new Color[] { redClear, redClear, new Color(150, 0, 0, a) }));
			g2.fillRect(0, 0, w, h);
		}

		// ---- Film grain: a few faint scan lines ----
		g2.setColor(new Color(255, 255, 255, 14));
		for (int i = 0; i < 6; i++)
		{
			g2.fillRect(0, rand.nextInt(h), w, 1);
		}

		// ---- Heartbeat ----
		long interval = (long) (1500 - 1000 * danger);
		if (now - lastBeat >= interval && heartClip != null && !heartClip.isRunning())
		{
			lastBeat = now;
			play(heartClip, (float) (-28 + 24 * danger));
		}

		// ---- Whispers ----
		if (now >= nextWhisperAt && now >= whisperUntil)
		{
			whisper = WHISPERS[rand.nextInt(WHISPERS.length)];
			whisperStart = now;
			whisperUntil = now + 2200;
			whisperX = 40 + rand.nextInt(Math.max(1, w - 360));
			whisperY = 80 + rand.nextInt(Math.max(1, h - 160));
			nextWhisperAt = now + 9000 + rand.nextInt(9000);
		}
		if (now < whisperUntil)
		{
			double t = (now - whisperStart) / (double) (whisperUntil - whisperStart);
			double fade = 1.0 - Math.abs(t * 2 - 1);      // fade in, then out
			int alpha = (int) (fade * 150);
			Font wf = (font != null) ? font.deriveFont(24f) : new Font(Font.SERIF, Font.ITALIC, 28);
			g2.setFont(wf);
			g2.setColor(new Color(190, 0, 0, Math.max(0, Math.min(255, alpha))));
			g2.drawString(whisper, whisperX, whisperY);
		}
	}

	/** Dark vignette and a breathing line of text for the title screen. */
	public void drawTitle(Graphics2D g2, int w, int h, Font font)
	{
		silence();
		if (!enabled)
		{
			return;
		}
		long now = System.currentTimeMillis();
		g2.setPaint(new RadialGradientPaint(
				new Point2D.Float(w / 2f, h / 2f), Math.max(w, h) * 0.7f,
				new float[] { 0f, 0.4f, 1f },
				new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 90), new Color(0, 0, 0, 225) }));
		g2.fillRect(0, 0, w, h);

		int alpha = (int) (110 + 60 * Math.sin(now / 700.0));
		Font tf = (font != null) ? font.deriveFont(20f) : new Font(Font.SERIF, Font.ITALIC, 22);
		g2.setFont(tf);
		g2.setColor(new Color(170, 0, 0, Math.max(0, Math.min(255, alpha))));
		g2.drawString("don't look back.", Math.max(20, w / 2 - 130), h - 40);
	}

	/** Played once when the player dies. Call it every repaint, it only fires once. */
	public void deathSting()
	{
		silence();
		if (!enabled || deathStingPlayed)
		{
			return;
		}
		deathStingPlayed = true;
		play(scareClip, 0f);
	}

	/** Stop heartbeat and drone, and reset the timers for the next run. */
	public void silence()
	{
		if (droneClip != null && droneClip.isRunning())
		{
			droneClip.stop();
		}
		if (heartClip != null && heartClip.isRunning())
		{
			heartClip.stop();
		}
		droneOn = false;
		started = false;
	}

	// ------------------------------------------------------------------
	// Sound generation (everything is made from math, no files needed)
	// ------------------------------------------------------------------

	private void play(Clip c, float gainDb)
	{
		if (c == null)
		{
			return;
		}
		try
		{
			if (c.isRunning())
			{
				c.stop();
			}
			setGain(c, gainDb);
			c.setFramePosition(0);
			c.start();
		}
		catch (Exception e)
		{
			// ignore sound problems
		}
	}

	private static void setGain(Clip c, float db)
	{
		try
		{
			FloatControl gc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			gc.setValue(Math.max(gc.getMinimum(), Math.min(gc.getMaximum(), db)));
		}
		catch (Exception e)
		{
			// gain control not supported on this system
		}
	}

	private static Clip makeClip(byte[] data) throws Exception
	{
		AudioFormat fmt = new AudioFormat(RATE, 16, 1, true, false);
		Clip c = AudioSystem.getClip();
		c.open(fmt, data, 0, data.length);
		return c;
	}

	private static byte[] toBytes(double[] s)
	{
		byte[] b = new byte[s.length * 2];
		for (int i = 0; i < s.length; i++)
		{
			int v = (int) Math.max(-32767, Math.min(32767, s[i] * 32767));
			b[2 * i] = (byte) (v & 0xff);
			b[2 * i + 1] = (byte) ((v >> 8) & 0xff);
		}
		return b;
	}

	private static double thump(double t, double amp)
	{
		if (t < 0)
		{
			return 0;
		}
		return amp * Math.sin(2 * Math.PI * 58 * t) * Math.exp(-t * 16);
	}

	/** "lub-dub": two low thumps close together. */
	private static byte[] makeHeartbeat()
	{
		double[] s = new double[(int) (RATE * 0.6)];
		for (int i = 0; i < s.length; i++)
		{
			double t = i / RATE;
			s[i] = 0.9 * (thump(t, 1.0) + thump(t - 0.22, 0.7));
		}
		return toBytes(s);
	}

	/** A low, slowly wobbling hum. Four seconds that loop without a click. */
	private static byte[] makeDrone()
	{
		Random r = new Random(7);
		double[] s = new double[(int) (RATE * 4)];
		for (int i = 0; i < s.length; i++)
		{
			double t = i / RATE;
			double wobble = 0.6 + 0.4 * Math.sin(2 * Math.PI * 0.25 * t);
			s[i] = 0.35 * Math.sin(2 * Math.PI * 50 * t)
					+ 0.20 * wobble * Math.sin(2 * Math.PI * 75 * t)
					+ 0.03 * (r.nextDouble() * 2 - 1);
		}
		return toBytes(s);
	}

	/** A falling screech mixed with noise. This one is LOUD, keep your volume sensible. */
	private static byte[] makeScare()
	{
		Random r = new Random(11);
		double[] s = new double[(int) (RATE * 0.8)];
		double phase = 0;
		for (int i = 0; i < s.length; i++)
		{
			double t = i / RATE;
			double freq = 900 - 800 * (t / 0.8);
			phase += 2 * Math.PI * freq / RATE;
			double env = Math.min(1.0, t * 80) * Math.exp(-t * 3);
			double tone = Math.sin(phase) + 0.5 * Math.sin(2 * phase);
			double noise = r.nextDouble() * 2 - 1;
			s[i] = 0.85 * env * (0.45 * tone + 0.55 * noise);
		}
		return toBytes(s);
	}
}
