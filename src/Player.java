import javax.swing.ImageIcon;

public class Player 
{
	//fields
	private int xPos, yPos;
	private ImageIcon imgPlayer,imgSword;
	private int dir;
	private final int UP = 0, DOWN = 1,LEFT = 2,RIGHT = 3;

	//Constructors
	public Player()
	{
		xPos = 0;
		yPos = 0;
		imgPlayer = new ImageIcon ("pictures/IdlePlayer.png");
		imgSword = new ImageIcon("pictures/TechSwordLeft.png");
		dir = UP;
	}

	//Methods
	public void setY (int y)
	{
		yPos = y;
	}
	public void setX(int x)	
	{
		xPos = x;
	}
	public int getX()
	{
		return xPos;
	}
	public int getY()
	{
		return yPos;
	}
	public int yMove()
	{
		if(dir == UP)
		{
			yPos -= 10;
		}
		if(dir == DOWN)
		{
			yPos += 10;
		}
		return yPos;
	}
	public int xMove()
	{
		if(dir == RIGHT)
		{
			xPos += 10;
		}
		if(dir == LEFT)
		{
			xPos -= 10;
		}
		return xPos;
	}
	public ImageIcon getImage ()
	{
		return imgPlayer;
	}
	public ImageIcon getSword()
	{
		return imgSword;
	}
	public void setDirection (int y)
	{
		dir = y;
	}
	public void setImage (int choice)
	{
		if (choice == 0)
		{
			imgPlayer = new ImageIcon ("pictures/MovingPlayerLeft.png");
		}
		if (choice == 1)
		{
			imgPlayer = new ImageIcon ("pictures/ShootingPlayer.png");
		}
		if (choice == 2)
		{
			imgPlayer = new ImageIcon ("pictures/MovingPlayer.png");
		}
		if (choice == 3)
		{
			imgPlayer = new ImageIcon ("pictures/IdlePlayer.png");
		}
	}
	public void setSword (int choice)
	{
		if (choice == 0)
		{
			imgSword = new ImageIcon ("pictures/TechSwordLeft.png");
		}
		if (choice == 1)
		{
			imgSword = new ImageIcon ("pictures/TechSwordRight.png");
		}
	}
}
