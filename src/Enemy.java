import javax.swing.ImageIcon;

public class Enemy 
{
	//fields
	private int xPos, yPos;
	private ImageIcon imgEnemy, imgEnemy2;
	private int dir;
	private final int UP = 0, DOWN = 1,LEFT = 2,RIGHT = 3;
	
	//Constructors
	public Enemy()
	{
		xPos = 0;
		yPos = 0;
		imgEnemy = new ImageIcon ("pictures/EnemiesLevel1.png");
		imgEnemy2 = new ImageIcon("pictures/EnemiesLevel2.png");
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
				yPos -= 3	;
			}
			if(dir == DOWN)
			{
				yPos += 3;
			}
			return yPos;
		}
		public int xMove()
		{
			if(dir == RIGHT)
			{
				xPos += 3;
			}
			if(dir == LEFT)
			{
				xPos -= 3;
			}
			return xPos;
		}
		public ImageIcon getImage(int num)
		{
			if (num == 1)
			{
				return imgEnemy;
			}
			else
			{
				return imgEnemy2;
			}
			
		}
		
		public void setDirection (int y)
		{
			dir = y;
		}
	
	
}
