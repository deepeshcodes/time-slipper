import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Turns the three still pictures of the player into an animation.
 *
 * The art we have:
 *   IdlePlayer.png        standing still (facing right)
 *   MovingPlayer.png      a stride pose, facing right
 *   MovingPlayerLeft.png  a stride pose, facing left
 *
 * Walking: the character alternates  stride -> standing -> stride -> standing,
 * which reads as legs passing each other, and bobs up and down on every step.
 * Standing still: the character "breathes" (a very slight stretch up and down).
 * A shadow under the feet makes the bobbing look like a real step.
 *
 * Everything is timed with the clock, not with a frame counter, so it looks the
 * same no matter how often the game repaints.
 */
public class PlayerAnimator
{
	private BufferedImage idleRight, idleLeft, walkRight, walkLeft;

	// Tweak these to change how the animation feels
	private static final int FRAME_MS = 100;    // how long each walk frame lasts
	private static final int BOB_PIXELS = 5;    // how high each step bounces
	private static final double BREATH = 0.015; // how much he stretches when idle (1.5%)

	public PlayerAnimator()
	{
		try
		{
			idleRight = ImageIO.read(new File("pictures/IdlePlayer.png"));
			walkRight = ImageIO.read(new File("pictures/MovingPlayer.png"));
			walkLeft = ImageIO.read(new File("pictures/MovingPlayerLeft.png"));
			idleLeft = flip(idleRight);
		}
		catch (Exception e)
		{
			System.out.println("PlayerAnimator: could not load player pictures (" + e + ")");
			idleRight = null;
			idleLeft = null;
			walkRight = null;
			walkLeft = null;
		}
	}

	/** False if the pictures could not be loaded; the game then draws the old still image. */
	public boolean isReady()
	{
		return idleRight != null && idleLeft != null && walkRight != null && walkLeft != null;
	}

	/**
	 * Draw the player.
	 *
	 * @param x,y         top-left corner of the player (same numbers the game already uses)
	 * @param moving      true while any movement key is held
	 * @param facingRight which way he is facing
	 * @param now         System.currentTimeMillis()
	 */
	public void draw(Graphics2D g2, int x, int y, boolean moving, boolean facingRight, long now)
	{
		BufferedImage frame;
		int bob = 0;
		double stretch = 1.0;

		if (moving)
		{
			int step = (int) ((now / FRAME_MS) % 4);
			BufferedImage stride = facingRight ? walkRight : walkLeft;
			BufferedImage passing = facingRight ? idleRight : idleLeft;
			frame = (step % 2 == 0) ? stride : passing;

			// Highest point in the middle of the "passing" frame, lowest on the stride frames
			double phase = ((now + FRAME_MS + FRAME_MS / 2) % (2 * FRAME_MS)) / (double) (2 * FRAME_MS);
			bob = (int) Math.round(BOB_PIXELS * Math.sin(Math.PI * phase));
		}
		else
		{
			frame = facingRight ? idleRight : idleLeft;
			stretch = 1.0 + BREATH * Math.sin(now / 450.0);
		}

		int w = frame.getWidth();
		int h = frame.getHeight();
		int drawH = (int) Math.round(h * stretch);

		// Shadow on the ground, smaller while he is in the air
		int shadowW = 62 - bob * 3;
		g2.setColor(new Color(0, 0, 0, 70));
		g2.fillOval(x + (w - shadowW) / 2, y + h - 9, shadowW, 12);

		// Keep pixel art crisp when it is stretched, then put the setting back
		Object oldHint = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		// Anchor at the feet: stretching grows the head upward, the feet stay on the ground
		g2.drawImage(frame, x, y + (h - drawH) - bob, w, drawH, null);

		if (oldHint != null)
		{
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldHint);
		}
	}

	private static BufferedImage flip(BufferedImage src)
	{
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		g.drawImage(src, w, 0, -w, h, null);
		g.dispose();
		return out;
	}
}
