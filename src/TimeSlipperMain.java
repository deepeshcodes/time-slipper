
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TimeSlipperMain extends JPanel implements ActionListener, KeyListener,MouseListener
{

	private ImageIcon BackButton, WizardImage, WASDIMG, commonbackgroundImg, 
	bossimg,titlepage,playButton,controlsButton,level1,level2, deathScreen, sleepScreen,
	blackBackground,wizard,wizardIcon,playerIcon, BossBackground, bossIcon, spacebarImg,heart1,heart2,heart3,bossBall, winScreen;
	private ImageIcon[] enemyimg1,enemyimg2;
	private Timer storyTimer,titleTimer,playerTimer, enemyLevel1Timer,enemyLevel2Timer,bossTimer,invisTimer, bossShootTimer,bulletTimer;	
	private final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	private int playerX,playerY,wizardX,wizardY,option,enemy1X,enemy1Y,
	enemy2X,enemy2Y,enemy3X,enemy3Y,enemy4X,enemy4Y,enemy5X,enemy5Y,enemy6X,
	enemy6Y,enemy7X,enemy7Y,enemiesLeft,swordX,swordY,enemiesLeftLevel2,bossX,bossY,bossLives, chance ,
	invisCount,bossBallX,bossBallY,randomNumber, deathScreenX, deathScreenY, deathScreenWidth, deathScreenHeight,tryCounts;
	private String[] names,buttons;
	private String timeCodeInput,rememberHomeInput;
	private JFrame frame;
	private Font f, f2;
	private Rectangle2D playMask,controlsMask, gobackMask, wizardMask,bossMask, playerMask,enemy1Mask,enemy2Mask,enemy3Mask,enemy4Mask,enemy5Mask,enemy6Mask,enemy7Mask,swordMask,bossBallMask;
	private boolean play,controls,goback,up,down,left,right,nextLevel1,nextLevel2,bossLevel,dialog,dialogBoss, attack,rightHand,dead,sleep,isFired, spacePressed,playerMoving,dialogDone;
	private Player player;
	private Enemy enemy;
	private Clip bgMusic;
	private Horror horror;
	private PlayerAnimator animator;
	private boolean facingRight = true;
	private Random killRand,ballYRand;

	public TimeSlipperMain()
	{
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();

		player = new Player();
		enemy = new Enemy();

		storyTimer = new Timer(1000, this);
		titleTimer = new Timer(1000,this);
		invisTimer = new Timer(1,this);
		playerTimer = new Timer(50,this);
		enemyLevel1Timer = new Timer(60,this);
		enemyLevel2Timer = new Timer(60,this);
		bossTimer = new Timer(60, this);
		bossShootTimer = new Timer(3000,this);
		bulletTimer = new Timer(30,this);
		ballYRand = new Random();
		killRand = new Random();

		playerX = 0;
		playerY = 300;
		enemy1X = 30;
		enemy1Y = 500;
		enemy2X = 30;
		enemy2Y = 600;
		wizardX = 630;
		wizardY = 270;
		enemiesLeft = 2;
		enemiesLeftLevel2 = 5;
		swordX = -4000;
		swordY = -4000;
		enemy3X = 1000;
		enemy3Y = 600;
		enemy4X = 300;
		enemy4Y = 700;
		enemy5X = 50;
		enemy5Y = 100;
		enemy6X = 1400;
		enemy6Y = 250;
		enemy7X = 550;
		enemy7Y = 150;
		bossX = 150;
		bossY = 100;
		bossLives = 3;
		bossBallX = 100;
		bossBallY = -1000;
		randomNumber = 0;
		tryCounts = 3;

		bossimg = new ImageIcon("pictures/Boss.png");
		titlepage = new ImageIcon("pictures/background.png");
		spacebarImg = new ImageIcon("pictures/Spacebar.png");
		playButton = new ImageIcon("pictures/PlayButton.png");
		controlsButton = new ImageIcon("pictures/ControlsButton.png");
		level1 = new ImageIcon("pictures/Level1.png");
		level2 = new ImageIcon("pictures/Level2.png");
		WASDIMG = new ImageIcon("pictures/WASDTOMOVE.png");
		commonbackgroundImg = new ImageIcon("pictures/common background.png");
		BackButton = new ImageIcon("pictures/BackButton.png");
		blackBackground = new ImageIcon("pictures/BlackBackground.png");
		BossBackground = new ImageIcon("pictures/BossLevel.png");
		wizard = new ImageIcon("pictures/Wizard.png");
		wizardIcon = new ImageIcon("pictures/WizardIcon.png");
		playerIcon = new ImageIcon("pictures/playerIcon.png");
		bossIcon = new ImageIcon("pictures/bossIcon.png");
		heart1 = new ImageIcon("pictures/heart1.png");
		heart2 = new ImageIcon("pictures/heart2.png");
		heart3 = new ImageIcon("pictures/heart3.png");
		sleepScreen = new ImageIcon("pictures/failedScreen.png");
		deathScreen = new ImageIcon("pictures/Death Screen.png");
		bossBall = new ImageIcon("pictures/BossBall.png");
		winScreen = new ImageIcon("pictures/winScreen.png");


		play = false;
		controls = false;
		goback = false;
		up = false;
		down = false;
		left = false;
		right = false;
		nextLevel1 = false;
		nextLevel2 = false;
		bossLevel = false;
		dialog = false;
		dialogBoss = false;
		attack = false;
		rightHand = false;
		dead = false;
		sleep = false;
		isFired = false;
		dialogDone = false;

		buttons = new String[]{"So why im I here?","I don’t remember what happened"};


		enemyimg1 = new ImageIcon[2];
		enemyimg2 = new ImageIcon[5];
		for (int i = 0; i < 2; i++)
		{
			enemyimg1[i] = enemy.getImage(1);
		}
		for (int i = 0; i < 5; i++)
		{
			enemyimg2[i] = enemy.getImage(2);
		}
		try
		{
			f = Font.createFont(Font.TRUETYPE_FONT,new File("SuperLegendBoy.ttf")).deriveFont(50f);
			f2 = Font.createFont(Font.TRUETYPE_FONT,new File("SuperLegendBoy.ttf")).deriveFont(30f);

		}
		catch(IOException | FontFormatException e)
		{
			System.out.print("cannot get font");
		}


		playMask = new Rectangle2D.Double(300,400,playButton.getIconWidth(),playButton.getIconHeight());
		controlsMask = new Rectangle2D.Double(300,300,controlsButton.getIconWidth(),controlsButton.getIconHeight());
		wizardMask = new Rectangle2D.Double(wizardX,wizardY,wizard.getIconWidth(),wizard.getIconHeight());
		playerMask = new Rectangle2D.Double(playerX+50,playerY+20,player.getImage().getIconWidth()-100,player.getImage().getIconHeight() - 50);
		enemy1Mask = new Rectangle2D.Double(enemy1X, enemy1Y,enemy.getImage(1).getIconWidth(), enemy.getImage(1).getIconHeight());
		enemy2Mask = new Rectangle2D.Double(enemy2X, enemy2Y,enemy.getImage(1).getIconWidth(), enemy.getImage(1).getIconHeight());
		enemy3Mask = new Rectangle2D.Double(enemy3X, enemy3Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy4Mask = new Rectangle2D.Double(enemy4X, enemy4Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy5Mask = new Rectangle2D.Double(enemy5X, enemy5Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy6Mask = new Rectangle2D.Double(enemy6X, enemy6Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy7Mask = new Rectangle2D.Double(enemy7X, enemy7Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		swordMask = new Rectangle2D.Double(swordX,swordY,player.getSword().getIconWidth(),player.getSword().getIconHeight());
		bossMask = new Rectangle2D.Double(bossX, bossY, bossimg.getIconWidth(), bossimg.getIconHeight());
		bossBallMask = new Rectangle2D.Double(bossBallX,bossBallY,bossBall.getIconWidth(),bossBall.getIconHeight());

		//MUSIC (WE'LL ADD IT AT THE END - TOO MUCH MEMORY)		{
		//			try
		//
		//			{
		//				Open an audio input stream.
		//				File soundFile2 = new File("sounds/bgMusic.wav");
		//				AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(soundFile2);
		//				bgMusic = AudioSystem.getClip();
		//				bgMusic.open(audioIn2);
		//				bgMusic.start();
		//				bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		//			}
		//			catch(Exception e)
		//			{
		//				JOptionPane.showMessageDialog(null, "File not Found");
		//			}
		//		}


		horror = new Horror();
		animator = new PlayerAnimator();
		JOptionPane.showMessageDialog(null,
				"WARNING: this game contains darkness, a loud jumpscare and a heartbeat sound.\n"
				+ "Turn your volume down a little before you start.\n\n"
				+ "Press H at any time to switch the horror effects on or off.",
				"Time Slipper - Content Warning", JOptionPane.WARNING_MESSAGE);

		frame = new JFrame();
		frame.setContentPane(this);
		frame.setSize(titlepage.getIconWidth(),titlepage.getIconHeight());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		titleTimer.start();
	}
	public static void main(String[] args) 
	{
		new TimeSlipperMain();

	}
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;

		if(dead == true && (nextLevel1 == true || nextLevel2 == true || bossLevel == true || dialogDone))
		{
			playerX = -8000;
			wizardX = -8000;

			deathScreenX = 0;
			deathScreenY = 0;
			deathScreenWidth = getWidth(); // Set to the width of the frame
			deathScreenHeight = getHeight(); // Set to the height of the frame
			g2.drawImage(deathScreen.getImage(), deathScreenX, deathScreenY, deathScreenWidth, deathScreenHeight, this);
			horror.deathSting();
		}
		else if(sleep == true && (nextLevel1 == true || nextLevel2 == true || bossLevel == true))
		{
			playerX = -8000;
			wizardX = -8000;

			deathScreenX = 0;
			deathScreenY = 0;
			deathScreenWidth = getWidth(); // Set to the width of the frame
			deathScreenHeight = getHeight(); // Set to the height of the frame

			g2.drawImage(sleepScreen.getImage(),deathScreenX,deathScreenY, deathScreenWidth, deathScreenHeight, this);
			horror.deathSting();
		}
		else
		{
			if(play == false && controls == false && nextLevel1 == false)
			{
				g2.drawImage(titlepage.getImage(),0,0,this);
				g2.drawImage(playButton.getImage(),300,400,this);
				g2.drawImage(controlsButton.getImage(),300,500,this);
				g2.setFont(f);
				g2.setColor(new Color(170, 0, 0));
				g2.drawString("Time Slipper",200,200);
				horror.drawTitle(g2, getWidth(), getHeight(), f2);
			}
			if(play == true)
			{
				g2.drawImage(blackBackground.getImage(),0,0,this);
				drawPlayer(g2);
				g2.drawImage(wizard.getImage(),wizardX,wizardY,this);
				g2.setColor(Color.yellow);
				playerMask = new Rectangle2D.Double(playerX+50,playerY+20,player.getImage().getIconWidth()-100,player.getImage().getIconHeight() - 50);
				wizardMask = new Rectangle2D.Double(wizardX,wizardY,wizard.getIconWidth(),wizard.getIconHeight());
			}
			else if(controls == true)
			{

				g2.drawImage(commonbackgroundImg.getImage(), 0, 0, this);
				g2.setFont(f);
				g2.setColor(Color.yellow);
				g2.drawString("CONTROLS",193,100);
				g2.drawImage(WASDIMG.getImage(), 45, 220, this);
				g2.setFont(f2);
				g2.setColor(Color.black);
				g2.drawString("TO MOVE",100,220);
				g2.drawString("TO ATTACK",430,220);
				gobackMask = new Rectangle2D.Double(605,-15,BackButton.getIconWidth(),BackButton.getIconHeight());
				g2.drawImage(BackButton.getImage(),605,-15,this);
				g2.drawImage(spacebarImg.getImage(), 400, 220, this);

			}
			if(nextLevel1 == true)
			{
				g2.drawImage(level1.getImage(),0,0,this);
				drawPlayer(g2);
				g2.drawImage(enemyimg1[0].getImage(),enemy1X,enemy1Y,this);
				g2.drawImage(enemyimg1[1].getImage(),enemy2X,enemy2Y,this);
				g2.draw(enemy1Mask);
				g2.draw(enemy2Mask);
				g2.draw(playerMask);
				g2.draw(swordMask);
			}

			if(nextLevel2 == true)
			{
				g2.drawImage(level2.getImage(),0,0,this);
				drawPlayer(g2);
				g2.drawImage(enemyimg2[0].getImage(),enemy3X,enemy3Y,this);
				g2.drawImage(enemyimg2[1].getImage(),enemy4X,enemy4Y,this);
				g2.drawImage(enemyimg2[2].getImage(),enemy5X,enemy5Y,this);
				g2.drawImage(enemyimg2[3].getImage(),enemy6X,enemy6Y,this);
				g2.drawImage(enemyimg2[4].getImage(),enemy7X,enemy7Y,this);
			}

			if(bossLevel == true)
			{
				g2.drawImage(BossBackground.getImage(), 0, 0, this);

				drawPlayer(g2);
				playerMask = new Rectangle2D.Double(playerX+50,playerY+20,player.getImage().getIconWidth()-100,player.getImage().getIconHeight()-50);
				g2.draw(playerMask);
				g2.draw(bossMask);
				g2.drawImage(bossimg.getImage(), bossX, bossY, this);
				g2.drawImage(enemyimg1[0].getImage(),enemy1X,enemy1Y,this);
				g2.drawImage(enemyimg1[1].getImage(),enemy2X,enemy2Y,this);
				g2.drawImage(bossBall.getImage(),bossBallX,bossBallY,this);
				if(bossLives == 3)
				{
					g2.drawImage(heart1.getImage(),100,700,this);
					g2.drawImage(heart2.getImage(),175,700,this);
					g2.drawImage(heart3.getImage(),250,700,this);
				}
				if(bossLives == 2)
				{
					g2.drawImage(heart1.getImage(),100,700,this);
					g2.drawImage(heart2.getImage(),175,700,this);
				}
				if(bossLives == 1)	
				{
					g2.drawImage(heart1.getImage(),100,700,this);
				}
				if(bossLives == 0)
				{
					playerX = -9000;
					bossBallX = 2000000;
					bossBallY = 2000;
					deathScreenX = 0;
					deathScreenY = 0;
					deathScreenWidth = getWidth(); // Set to the width of the frame
					deathScreenHeight = getHeight(); // Set to the height of the frame
					g2.drawImage(winScreen.getImage(), deathScreenX, deathScreenY, deathScreenWidth, deathScreenHeight, this);
				}
			}	
			if (nextLevel1 == true || nextLevel2 == true || bossLevel == true) 
			{
				if (attack == true) 
				{
					if (rightHand == false) 
					{
						swordMask = new Rectangle2D.Double(playerX - 20,playerY + 4, player.getSword().getIconWidth(), player.getSword().getIconHeight());
						g2.drawImage(player.getSword().getImage(), playerX - 20, playerY + 4, this);
					} 
					else if (rightHand == true) 
					{
						swordMask = new Rectangle2D.Double(playerX + 70,playerY + 20, player.getSword().getIconWidth(), player.getSword().getIconHeight());
						g2.drawImage(player.getSword().getImage(), playerX + 70, playerY + 20, this);
					}
				}
				if (spacePressed == false)
				{
					swordMask = new Rectangle2D.Double(-9000, 1500, player.getSword().getIconWidth(), player.getSword().getIconHeight());
				}
			}

			// HORROR LAYER: drawn last so it sits on top of everything
			if (play == true && !(bossLevel == true && bossLives <= 0))
			{
				horror.draw(g2, getWidth(), getHeight(), playerX, playerY, computeDanger(), bossimg.getImage(), f2);
			}
		}
	}

	// Draws the player with a walk cycle while a movement key is held, and a still pose otherwise.
	private void drawPlayer(Graphics2D g2)
	{
		if (animator != null && animator.isReady())
		{
			boolean moving = up || down || left || right;
			animator.draw(g2, playerX, playerY, moving, facingRight, System.currentTimeMillis());
		}
		else
		{
			g2.drawImage(player.getImage().getImage(), playerX, playerY, this);
		}
	}

	// How close is the nearest threat? 0.0 = far away, 1.0 = touching the player.
	private double computeDanger()
	{
		int[][] threats;
		if (nextLevel1 == true)
		{
			threats = new int[][] {{enemy1X, enemy1Y}, {enemy2X, enemy2Y}};
		}
		else if (nextLevel2 == true)
		{
			threats = new int[][] {{enemy3X, enemy3Y}, {enemy4X, enemy4Y}, {enemy5X, enemy5Y}, {enemy6X, enemy6Y}, {enemy7X, enemy7Y}};
		}
		else if (bossLevel == true)
		{
			threats = new int[][] {{bossX, bossY}, {enemy1X, enemy1Y}, {enemy2X, enemy2Y}, {bossBallX, bossBallY}};
		}
		else
		{
			return 0.0;
		}
		int px = playerX + player.getImage().getIconWidth() / 2;
		int py = playerY + player.getImage().getIconHeight() / 2;
		double nearest = 100000;
		for (int[] t : threats)
		{
			double d = Math.hypot(t[0] - px, t[1] - py);
			if (d < nearest)
			{
				nearest = d;
			}
		}
		return Math.max(0.0, Math.min(1.0, 1.0 - nearest / 450.0));
	}
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == titleTimer)
		{
			if(play == true)
			{
				frame.setSize(blackBackground.getIconWidth(),blackBackground.getIconHeight());
				frame.setLocationRelativeTo(null);
				playerTimer.start();
			}
			if(controls == true)
			{
				frame.setSize(800,800);
				frame.setLocationRelativeTo(null);
			}
		}
		if(e.getSource() == playerTimer)
		{
			player.setX(playerX);
			player.setY(playerY);
			if(up)
			{
				if(bossLevel == true)
				{
					if(playerY <=180)
					{
						playerY-=0;
					}
					else
					{
						player.setDirection(UP);
						playerY = player.yMove();
					}
				}
				if(playerY <= 200)
				{
					playerY -= 0;
				}
				else
				{
					player.setDirection(UP);
					playerY = player.yMove();
				}
			}
			if(down)
			{
				if(playerY + playerIcon.getIconHeight() >= getHeight())
				{
					playerY -= 0;
				}
				else
				{
					player.setDirection(DOWN);
					playerY = player.yMove();
				}
			}
			if(left)
			{
				if(playerX <= 0)
				{
					playerX -= 0;
				}
				else
				{
					player.setDirection(LEFT);
					playerX = player.xMove();
				}
			}
			if(right)
			{
				if(playerX + playerIcon.getIconWidth() >= getWidth()) 
				{
					playerX -= 0;
				}
				else
				{
					player.setDirection(RIGHT);
					playerX = player.xMove();
				}
			}
			if(bossLevel)
			{
				if (playerMask.intersects(bossMask)) 
				{

					chance = killRand.nextInt(2) +1;
					if(chance == 1)
					{
						dead = true;
					}
					else if (chance == 2)
					{
						System.out.print("sleep");
						sleep = true;
					}

				}
			}
			if(nextLevel1 == true)
			{
				if(!playerMoving)
				{
					enemyLevel1Timer.setDelay(150);
				}
				else
				{
					enemyLevel1Timer.setDelay(35);
				}
			}
			if(nextLevel2 == true)
			{
				if(!playerMoving)
				{
					enemyLevel2Timer.setDelay(150);
				}
				else
				{
					enemyLevel2Timer.setDelay(35);
				}
			}
			
		}
		if(e.getSource () == enemyLevel1Timer)
		{
			if (player.getX() < enemy1X) 
			{
				enemy1X -= 8; 
			} 
			else 
			{
				enemy1X += 8; 
			}

			if (player.getY() < enemy1Y) 
			{
				enemy1Y -= 8; 
			} 
			else 
			{
				enemy1Y += 8; 
			}
			if (player.getX() < enemy2X) 
			{
				enemy2X -= 4; 
			} 
			else 
			{
				enemy2X += 4; 
			}

			if (player.getY() < enemy2Y) 
			{
				enemy2Y -= 4; 
			} 
			else 
			{
				enemy2Y += 4; 
			}

			if(enemy1Mask.intersects(swordMask) || enemy2Mask.intersects(swordMask))
			{
				enemiesLeft--;
				if(enemy1Mask.intersects(swordMask))
				{
					enemy1X -= -1000;
					enemy1Y -= -1000;
				}
				if(enemy2Mask.intersects(swordMask))
				{
					enemy2X -= -3000;
					enemy2Y -= -3000;
				}
			}
			if(enemy1Mask.intersects(playerMask) || enemy2Mask.intersects(playerMask)) //KILL PLAYER
			{
				chance = killRand.nextInt(2) +1;
				if(chance == 1)
				{
					dead = true;
				}
				else if (chance == 2)
				{
					sleep = true;
				}
			}

		}
		if(e.getSource () == enemyLevel2Timer)
		{
			if (player.getX() < enemy3X) 
			{
				enemy3X -= 5; 
			} else 
			{
				enemy3X += 6; 
			}

			if (player.getY() < enemy3Y) 
			{
				enemy3Y -= 5; 
			} 
			else 
			{
				enemy3Y += 6; 
			}
			if (player.getX() < enemy4X) 
			{
				enemy4X -= 3; 
			} 
			else 
			{
				enemy4X += 4; 
			}
			if (player.getY() < enemy4Y) 
			{
				enemy4Y -= 3; 
			} 
			else 
			{
				enemy4Y += 4; 
			}
			if (player.getX() < enemy5X) 
			{
				enemy5X -= 7; 
			}
			else 
			{
				enemy5X += 3; 
			}
			if (player.getY() < enemy5Y) 
			{
				enemy5Y -= 7; 
			} 
			else 
			{
				enemy5Y += 3; 
			}
			if (player.getX() < enemy6X) 
			{
				enemy6X -= 4; 
			} 
			else 
			{
				enemy6X += 4; 
			}
			if (player.getY() < enemy6Y) 
			{
				enemy6Y -= 4; 
			} 
			else 
			{
				enemy6Y += 4; 
			}
			if (player.getX() < enemy7X) 
			{
				enemy7X -= 3; 
			} 
			else 
			{
				enemy7X += 4; 
			}
			if (player.getY() < enemy7Y) 
			{
				enemy7Y -= 3; 
			} 
			else 
			{
				enemy7Y += 4; 
			}

			if(enemy1Mask.intersects(playerMask) || enemy2Mask.intersects(playerMask) || enemy3Mask.intersects(playerMask) || enemy4Mask.intersects(playerMask) || enemy5Mask.intersects(playerMask) || enemy6Mask.intersects(playerMask) || enemy7Mask.intersects(playerMask)) //KILL PLAYER
			{
				chance = killRand.nextInt(2) +1;
				if(chance == 1)
				{
					dead = true;
				}
				else if (chance == 2)
				{
					sleep = true;
				}
			}
		}
		if(enemy3Mask.intersects(swordMask) || enemy4Mask.intersects(swordMask) || enemy5Mask.intersects(swordMask) || enemy6Mask.intersects(swordMask) || enemy7Mask.intersects(swordMask))
		{
			enemiesLeftLevel2--;
			if(enemy3Mask.intersects(swordMask))
			{
				enemy3X -= -1000;
				enemy3Y -= -1000;
			}
			if(enemy4Mask.intersects(swordMask))
			{
				enemy4X -= -1000;
				enemy4Y -= -1000;
			}
			if(enemy5Mask.intersects(swordMask))
			{
				enemy5X -= -1000;
				enemy5Y -= -1000;
			}
			if(enemy6Mask.intersects(swordMask))
			{
				enemy6X -= -1000;
				enemy6Y -= -1000;
			}
			if(enemy7Mask.intersects(swordMask))
			{
				enemy7X -= -1000;
				enemy7Y -= -1000;
			}
		}

		if(e.getSource()== bossTimer && !dialogBoss)
		{
			playerTimer.stop();
			TextBoxBoss();
			dialogBoss = true;
			playerTimer.start();
		}

		if(e.getSource()== bossTimer && dialogBoss)
		{
			// TESTING POSSIBLE HENCHMENS ----- - - - - - - - - - -- - - 
			bossShootTimer.start();
			if (player.getX() < enemy1X) 
			{
				enemy1X -= 4; 
			} else 
			{
				enemy1X += 4; 
			}

			if (player.getY() < enemy1Y) 
			{
				enemy1Y -= 4; 
			} 
			else 
			{
				enemy1Y += 4; 
			}
			if (player.getX() < enemy2X) 
			{
				enemy2X -= 4; 
			} 
			else 
			{
				enemy2X += 4; 
			}

			if (player.getY() < enemy2Y) 
			{
				enemy2Y -= 4; 
			} 
			else 
			{
				enemy2Y += 4; 
			}
			if(enemy1Mask.intersects(swordMask) || enemy2Mask.intersects(swordMask))
			{
				enemiesLeft--;
				System.out.print(enemiesLeft);
				if(enemy1Mask.intersects(swordMask))
				{
					enemy1X -= -1000;
					enemy1Y -= -1000;
				}
				if(enemy2Mask.intersects(swordMask))
				{
					enemy2X -= -3000;
					enemy2Y -= -3000;
				}
			}
			if(swordMask.intersects(bossMask))
			{
				bossLives--;
				invisTimer.start();
			}
			// TESTING POSSIBLE HENCHMENS ----- -------- --- - - -- - - - - - - - - 
			if(enemy1Mask.intersects(playerMask) || enemy2Mask.intersects(playerMask) || enemy3Mask.intersects(playerMask) || enemy4Mask.intersects(playerMask) || enemy5Mask.intersects(playerMask) || enemy6Mask.intersects(playerMask) || enemy7Mask.intersects(playerMask)) //KILL PLAYER
			{
				chance = killRand.nextInt(2) +1;
				if(chance == 1)
				{
					dead = true;
				}
				else if (chance == 2)
				{
					sleep = true;
				}
			}
		}
		if(e.getSource() == bossShootTimer)	
		{
			if(isFired == false)
			{
				bossBallX = 100;
				randomNumber = ballYRand.nextInt(518) + 100;
				isFired = true;
				bulletTimer.start();
			}
		}
		if(e.getSource() == bulletTimer)
		{
			bossBallX += 10;
			bossBallY = randomNumber;
			if(isFired == true && bossBallX >= getWidth())
			{
				isFired = false;
			}

			if(bossBallMask.intersects(playerMask))
			{
				chance = killRand.nextInt(2) +1;
				if(chance == 1)
				{
					dead = true;
				}
				else if (chance == 2)
				{
					System.out.print("sleep");
					sleep = true;
				}
			}
		}
		if(e.getSource() == invisTimer)	
		{
			bossMask = new Rectangle2D.Double(bossX - 1000, bossY -1000, bossimg.getIconWidth(), bossimg.getIconHeight());
			invisCount++;
			if(invisCount == 100)
			{
				bossMask = new Rectangle2D.Double(bossX,bossY,bossimg.getIconWidth(), bossimg.getIconHeight());
				invisTimer.stop();
			}
			if(invisCount == 200)
			{
				bossMask = new Rectangle2D.Double(bossX,bossY,bossimg.getIconWidth(), bossimg.getIconHeight());
				invisTimer.stop();
			}
			if(invisCount == 300)
			{
				bossMask = new Rectangle2D.Double(bossX,bossY,bossimg.getIconWidth(), bossimg.getIconHeight());
				invisTimer.stop();
			}
		}
		if(enemiesLeft == 0)
		{
			enemyLevel1Timer.stop();
			nextLevel1 = false;
			nextLevel2 = true;
			enemyLevel2Timer.start();
		}
		if(enemiesLeftLevel2 == 0)
		{
			enemyLevel2Timer.stop();
			nextLevel2 = false;
			bossLevel = true;
			bossTimer.start();		
		}
		if(playerMask.intersects(wizardMask) && !dialog)
		{
			dialog = true;
			TextBox();
			playerTimer.stop();
			playerTimer.start();
			nextLevel1 = true;
			if(dialogDone)
			{
				enemyLevel1Timer.start();
			}
		}
		playMask = new Rectangle2D.Double(300,400,playButton.getIconWidth(),playButton.getIconHeight());
		controlsMask = new Rectangle2D.Double(300,300,controlsButton.getIconWidth(),controlsButton.getIconHeight());
		wizardMask = new Rectangle2D.Double(wizardX,wizardY,wizard.getIconWidth(),wizard.getIconHeight());
		playerMask = new Rectangle2D.Double(playerX+50,playerY+20,player.getImage().getIconWidth()-100,player.getImage().getIconHeight() - 50);
		enemy1Mask = new Rectangle2D.Double(enemy1X, enemy1Y,enemy.getImage(1).getIconWidth(), enemy.getImage(1).getIconHeight());
		enemy2Mask = new Rectangle2D.Double(enemy2X, enemy2Y,enemy.getImage(1).getIconWidth(), enemy.getImage(1).getIconHeight());
		enemy3Mask = new Rectangle2D.Double(enemy3X, enemy3Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy4Mask = new Rectangle2D.Double(enemy4X, enemy4Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy5Mask = new Rectangle2D.Double(enemy5X, enemy5Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy6Mask = new Rectangle2D.Double(enemy6X, enemy6Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		enemy7Mask = new Rectangle2D.Double(enemy7X, enemy7Y,enemy.getImage(2).getIconWidth(), enemy.getImage(2).getIconHeight());
		bossBallMask = new Rectangle2D.Double(bossBallX,bossBallY,bossBall.getIconWidth(),bossBall.getIconHeight());

		repaint();
	}
	public void TextBox()
	{
		
		up = false;
		down = false;
		left = false;
		right = false;
		while(true)
		{
			JOptionPane.showMessageDialog(null,"HEY, ARE YOU ONE OF THEM?!?!?!","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
			JOptionPane.showMessageDialog(null,"Who?","Cable",JOptionPane.DEFAULT_OPTION,playerIcon);
			timeCodeInput = JOptionPane.showInputDialog(null, "No - I can't trust you.\nWhat is your time code? (your time code: 9872681)",JOptionPane.PLAIN_MESSAGE);
			if ("9872681".equals(timeCodeInput)) 
			{
				dialogDone = true;
				break;
			}
			else
			{
				tryCounts--;
				if(tryCounts == 0)
				{
					dead = true;
					break;
				}
			}
		}
		if(dead == false)
		{
			JOptionPane.showMessageDialog(null,"Oh I know... You're Cable! How are you?","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
			JOptionPane.showMessageDialog(null,"How do you know my name?","Cable",JOptionPane.DEFAULT_OPTION,playerIcon);
			JOptionPane.showMessageDialog(null,"I know many things about you Cable\nbut thats not important","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
			option = JOptionPane.showOptionDialog(null,"you are between the past and present","Wizard",JOptionPane.DEFAULT_OPTION,0,wizardIcon,buttons,null);
			if(option == 0)
			{
				JOptionPane.showMessageDialog(null,"You are here because you possess an ability that could save your world","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
				JOptionPane.showMessageDialog(null,"You are able to stop time by standing still, giving you the power to dodge everything","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
				JOptionPane.showMessageDialog(null,"So who am I facing?","Cable",JOptionPane.DEFAULT_OPTION,playerIcon);
				JOptionPane.showMessageDialog(null,"the very thing that cause the destruction of your world…MechaFlyingGoblinRobot","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
				JOptionPane.showMessageDialog(null,"We are out of time, Cable... it already knows you're here.\nWhatever you hear in the dark... keep moving.","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
			}
			else if(option == 1)
			{
				JOptionPane.showMessageDialog(null,"Your world is in grave danger","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
				JOptionPane.showMessageDialog(null,"You are the only one who can save it","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
				JOptionPane.showMessageDialog(null,"GO, defeat MechaFlyingGoblinRobot, and find out what strength you have","Wizard",JOptionPane.DEFAULT_OPTION,wizardIcon);
			}
		}
		
		dialogDone = true;
	}
	public void TextBoxBoss()
	{
		up = false;
		down = false;
		left = false;
		right = false;
		JOptionPane.showMessageDialog(null,"Ah, Cable... I have watched you in the dark this whole time.\nI have been expecting you.","MechaFlyingGoblinRobot",JOptionPane.DEFAULT_OPTION,bossIcon);
		JOptionPane.showMessageDialog(null,"What do you know about this threat, and what's with these glitches in time?","Cable",JOptionPane.DEFAULT_OPTION,playerIcon);
		JOptionPane.showMessageDialog(null,"Time, my dear officer, is a delicate dance.\nYour glitches are but a side effect of meddling with the past.","MechaFlyingGoblinRobot",JOptionPane.DEFAULT_OPTION,bossIcon);
		JOptionPane.showMessageDialog(null,"I'll put an end to this, no matter the temporal hurdles.","Cable",JOptionPane.DEFAULT_OPTION,playerIcon);
		JOptionPane.showMessageDialog(null,"Unfortunately... your powers don't work here.\nTime may bend, but can it break? Let's find out, Cable.\nThe lights go out now.","MechaFlyingGoblinRobot",JOptionPane.DEFAULT_OPTION,bossIcon);
	}
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A)
		{
			playerMoving = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
		{
			down = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_A)
		{
			left = true;
			rightHand = false;
			facingRight = false;
			player.setImage(0);
			player.setSword(0);

		}
		else if(e.getKeyCode() == KeyEvent.VK_D)
		{
			right = true;
			rightHand = true;
			facingRight = true;
			player.setImage(2);
			player.setSword(1);

		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			attack = true;
			spacePressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_H)
		{
			Horror.enabled = !Horror.enabled;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A)
		{
			playerMoving = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_S)
		{
			down = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_A)
		{
			left = false;

		}
		else if(e.getKeyCode() == KeyEvent.VK_D)
		{
			right = false;

		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			attack = false;
			spacePressed = false;
		}
	}
	public void mousePressed(MouseEvent e) 
	{	
		if(e.getX() >= 300 && e.getX() <=300+playButton.getIconWidth() && e.getY() >= 400 && e.getY() <= 400+playButton.getIconHeight())
		{
			play = true;
		}
		if(e.getX() >= 300 && e.getX() <=300+controlsButton.getIconWidth() && e.getY() >= 500 && e.getY() <= 500+controlsButton.getIconHeight())
		{
			controls = true;
		}	
		if(e.getX() >= 300 && e.getX() <=605+BackButton.getIconWidth() && e.getY() >= -15 && e.getY() <= -15+BackButton.getIconHeight())
		{
			controls = false;
			play = false;
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
}	