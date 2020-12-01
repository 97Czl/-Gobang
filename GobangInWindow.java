import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;

public class GobangInWindow
{
	//定义棋盘，白子，黑子，和落点被鼠标选中的图像
	private BufferedImage table;
	private BufferedImage white;
	private BufferedImage black;
	private BufferedImage select;

	//定义棋盘的大小
	private static int TABLE_SIZE = 15;
	//定义棋盘的宽和高多少像素
	private static int TABLE_WIDTH = 700;
	private static int TABLE_HEIGHT = 700;
	//定义检测位置时的坐标偏移量
	private static int X_OFFSET = 14;
	private static int Y_OFFSET = 14                                                 ;
	
	//定义棋盘像素与棋盘大小之比
	private static int RATE_WIDTH = (TABLE_WIDTH - X_OFFSET * 2) / (TABLE_SIZE - 1);
	private static int RATE_HEIGHT = (TABLE_HEIGHT - Y_OFFSET * 2) / (TABLE_SIZE - 1);

	//用于存放棋盘落子点状态的二维数组,该点值为0，代表空位置，为1代表黑棋，为2代表白棋
	private int[][] board = new int[TABLE_SIZE][TABLE_SIZE];

	//定义五子棋窗口
	private Frame f = new Frame("五子棋游戏");
	//定义画布
	private ChessBoard chessBoard = new ChessBoard();
	//定义当前选中点的坐标
	private int selectX = -1;
	private int selectY = -1; 
	
	//表示当前是黑棋还是白棋，true为黑棋，false为白棋
	private static boolean FLAG = true;
	//表示当前是未结束0，黑子赢1，白子赢2，平局3
	private static int RESULT = 0;
	private static int SUMOFCHESS = 0;

	/**
	 *初始化棋盘
	 */ 
	public void InitBoard() throws Exception
	{
		//设置四种图案
		table = ImageIO.read(new File("board.jpg"));
		black = ImageIO.read(new File("black.jpg"));
		white = ImageIO.read(new File("white.jpg"));
		select = ImageIO.read(new File("select.jpg"));
		
		//对数组进行初始化.所有值赋为0
		for (var i = 0; i < TABLE_SIZE; i++)
		{
			for (var j = 0; j < TABLE_SIZE; j++)
			{
				board[i][j] = 0;
			}
		}

		//将棋盘设为固定大小，并添加监听器
		chessBoard.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		chessBoard.addMouseListener(new MouseAdapter()
		{
			//添加鼠标点击事件
			public void mouseClicked(MouseEvent e)
			{
				//将该点所属的位置的数组赋值
				var xPos = Math.round((e.getX() - X_OFFSET) / (RATE_WIDTH * 1.0f));
				var yPos = Math.round((e.getY() - Y_OFFSET) / (RATE_HEIGHT * 1.0f));
				if (RESULT == 0 && board[xPos][yPos] == 0)
				{
					board[xPos][yPos] = FLAG ? 1 : 2;
					SUMOFCHESS ++ ;
					if (SUMOFCHESS == TABLE_SIZE * TABLE_SIZE)
					{
						RESULT = 3;
					}
					//判断该点落子后是否取得胜利
					RESULT = ifVictory(xPos, yPos);
					FLAG = !FLAG;
				}
				//绘制整个棋盘
				chessBoard.repaint();
			}

			//当鼠标退出棋盘区时，复位选中的坐标
			public void mouseExited(MouseEvent e )
			{
				selectX = -1;
				selectY = -1;
				chessBoard.repaint();
			}
		});

		//为棋盘添加鼠标移动的监听器
		chessBoard.addMouseMotionListener(new MouseMotionAdapter()
		{
			//当鼠标移动时，获得所属的点的坐标
			public void mouseMoved(MouseEvent e)
			{
				if (RESULT == 0)
				{
					selectX = Math.round((e.getX() - X_OFFSET) / (RATE_WIDTH * 1.0f));
					selectY = Math.round((e.getY() - Y_OFFSET) / (RATE_HEIGHT * 1.0f));
					chessBoard.repaint();
				}
			}
		}); 

		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		});
		f.add(chessBoard);
		f.pack();
		f.setVisible(true);
	}

	/**
	 *判断当前落子时候获得胜利
	 *@param place 代表此时落子的坐标
	 *@param order 代表黑子和白子的区别，true为黑子，false为白子
	 *@return 返回是否获得胜利，true代表胜利，false代表未胜利 
	 */
	private int ifVictory(int row, int column)
	{
		//保存四个方向上的棋子
		String chessInColumn = "";
		String chessInrow = "";
		String chessInUp = "";
		String chessInDown = "";

		//成功的案例是连续五个相同棋子
		String chessInVictory = "" + board[row][column] + board[row][column] + board[row][column] + board[row][column] + board[row][column];

		//判断该点所处的四个方向是否构成连续的五子
		//先判断该行
		for (var i = -4; i < 5; i++)
		{
			if ((row + i) < 0 || (row + i) >= TABLE_SIZE )
			{}
			else 
			{
				chessInrow = chessInrow + board[row + i][column];
			};

			if ((column + i) < 0 || (column + i) >= TABLE_SIZE)
			{}
			else
			{
				chessInColumn = chessInColumn + board[row][column + i];
			};

			for (var j = -4; j < 5; j++)
			{
				if (i != j)
				{
					continue;
				};
				if (((column + j) < TABLE_SIZE && (column + j) >= 0) && ((row - i) < TABLE_SIZE && (row - i) >= 0))
				{
					chessInUp = chessInUp + board[row - i][column + j];
				};
				if (((column + j) < TABLE_SIZE && (column + j) >= 0) && ((row + i) < TABLE_SIZE && (row + i) >= 0))
				{
					chessInDown = chessInDown + board[row + i][column + j];
				};
			}
		}
		if (chessInColumn.contains(chessInVictory) || chessInrow.contains(chessInVictory) || chessInUp.contains(chessInVictory) || chessInDown.contains(chessInVictory) )
		{
			return FLAG ? 1 : 2;
		}
		else 
		{
			return 0;
		}
	}


	public static void main(String[] args) throws Exception
	{
		new GobangInWindow().InitBoard();
	}

	//内部类实现绘制画布
	private class ChessBoard extends JPanel
	{
		//重写JPanel的paint方法
		public void paint(Graphics g)
		{
			//绘制五子棋棋盘
			g.drawImage(table, 0, 0, TABLE_WIDTH, TABLE_HEIGHT, null);
			//绘制选中的红框
			if (selectX > 0 && selectY > 0)
			{
				//g.setColor(new Color(255, 0, 0));
				//g.setFont(new Font("宋体", Font.BOLD, 10));
				//g.drawString("(" + selectX + "," + selectY + ")", selectX, selectY);
				g.drawImage(select, selectX * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 8, selectY * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 8, RATE_WIDTH / 4, RATE_HEIGHT / 4, null);
			}  
			//遍历数组绘制棋子
			for (var i = 0; i < TABLE_SIZE; i++)
			{
				for (var j = 0; j < TABLE_SIZE; j++)
				{
					//绘制黑棋
					if (board[i][j] == 1)
					{
						g.drawImage(black, i * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 2, j * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 2, RATE_WIDTH , RATE_HEIGHT , null);
					}
					//绘制白棋
					if (board[i][j] == 2)
					{
						g.drawImage(white, i * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 2, j * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 2, RATE_WIDTH , RATE_HEIGHT , null);
					}
				}
			}
			if (RESULT == 1)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("宋体", Font.BOLD, 20));
				g.drawString("黑子取得胜利！！！", TABLE_WIDTH / 2 - 70, 20);
			}
			else if (RESULT == 2)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("宋体", Font.BOLD, 20));
				g.drawString("白子取得胜利！！！", TABLE_WIDTH / 2 - 70, 20);
			}
			else if (RESULT == 3)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("宋体", Font.BOLD, 20));
				g.drawString("平局！！！", TABLE_WIDTH / 2 - 40, 30);
			}
		}
	}
}
