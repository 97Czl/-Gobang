import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;

public class GobangInWindow
{
	//�������̣����ӣ����ӣ�����㱻���ѡ�е�ͼ��
	private BufferedImage table;
	private BufferedImage white;
	private BufferedImage black;
	private BufferedImage select;

	//�������̵Ĵ�С
	private static int TABLE_SIZE = 15;
	//�������̵Ŀ�͸߶�������
	private static int TABLE_WIDTH = 700;
	private static int TABLE_HEIGHT = 700;
	//������λ��ʱ������ƫ����
	private static int X_OFFSET = 14;
	private static int Y_OFFSET = 14                                                 ;
	
	//�����������������̴�С֮��
	private static int RATE_WIDTH = (TABLE_WIDTH - X_OFFSET * 2) / (TABLE_SIZE - 1);
	private static int RATE_HEIGHT = (TABLE_HEIGHT - Y_OFFSET * 2) / (TABLE_SIZE - 1);

	//���ڴ���������ӵ�״̬�Ķ�ά����,�õ�ֵΪ0�������λ�ã�Ϊ1������壬Ϊ2�������
	private int[][] board = new int[TABLE_SIZE][TABLE_SIZE];

	//���������崰��
	private Frame f = new Frame("��������Ϸ");
	//���廭��
	private ChessBoard chessBoard = new ChessBoard();
	//���嵱ǰѡ�е������
	private int selectX = -1;
	private int selectY = -1; 
	
	//��ʾ��ǰ�Ǻ��廹�ǰ��壬trueΪ���壬falseΪ����
	private static boolean FLAG = true;
	//��ʾ��ǰ��δ����0������Ӯ1������Ӯ2��ƽ��3
	private static int RESULT = 0;
	private static int SUMOFCHESS = 0;

	/**
	 *��ʼ������
	 */ 
	public void InitBoard() throws Exception
	{
		//��������ͼ��
		table = ImageIO.read(new File("board.jpg"));
		black = ImageIO.read(new File("black.jpg"));
		white = ImageIO.read(new File("white.jpg"));
		select = ImageIO.read(new File("select.jpg"));
		
		//��������г�ʼ��.����ֵ��Ϊ0
		for (var i = 0; i < TABLE_SIZE; i++)
		{
			for (var j = 0; j < TABLE_SIZE; j++)
			{
				board[i][j] = 0;
			}
		}

		//��������Ϊ�̶���С������Ӽ�����
		chessBoard.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		chessBoard.addMouseListener(new MouseAdapter()
		{
			//���������¼�
			public void mouseClicked(MouseEvent e)
			{
				//���õ�������λ�õ����鸳ֵ
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
					//�жϸõ����Ӻ��Ƿ�ȡ��ʤ��
					RESULT = ifVictory(xPos, yPos);
					FLAG = !FLAG;
				}
				//������������
				chessBoard.repaint();
			}

			//������˳�������ʱ����λѡ�е�����
			public void mouseExited(MouseEvent e )
			{
				selectX = -1;
				selectY = -1;
				chessBoard.repaint();
			}
		});

		//Ϊ�����������ƶ��ļ�����
		chessBoard.addMouseMotionListener(new MouseMotionAdapter()
		{
			//������ƶ�ʱ����������ĵ������
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
	 *�жϵ�ǰ����ʱ����ʤ��
	 *@param place �����ʱ���ӵ�����
	 *@param order ������ӺͰ��ӵ�����trueΪ���ӣ�falseΪ����
	 *@return �����Ƿ���ʤ����true����ʤ����false����δʤ�� 
	 */
	private int ifVictory(int row, int column)
	{
		//�����ĸ������ϵ�����
		String chessInColumn = "";
		String chessInrow = "";
		String chessInUp = "";
		String chessInDown = "";

		//�ɹ��İ��������������ͬ����
		String chessInVictory = "" + board[row][column] + board[row][column] + board[row][column] + board[row][column] + board[row][column];

		//�жϸõ��������ĸ������Ƿ񹹳�����������
		//���жϸ���
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

	//�ڲ���ʵ�ֻ��ƻ���
	private class ChessBoard extends JPanel
	{
		//��дJPanel��paint����
		public void paint(Graphics g)
		{
			//��������������
			g.drawImage(table, 0, 0, TABLE_WIDTH, TABLE_HEIGHT, null);
			//����ѡ�еĺ��
			if (selectX > 0 && selectY > 0)
			{
				//g.setColor(new Color(255, 0, 0));
				//g.setFont(new Font("����", Font.BOLD, 10));
				//g.drawString("(" + selectX + "," + selectY + ")", selectX, selectY);
				g.drawImage(select, selectX * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 8, selectY * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 8, RATE_WIDTH / 4, RATE_HEIGHT / 4, null);
			}  
			//���������������
			for (var i = 0; i < TABLE_SIZE; i++)
			{
				for (var j = 0; j < TABLE_SIZE; j++)
				{
					//���ƺ���
					if (board[i][j] == 1)
					{
						g.drawImage(black, i * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 2, j * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 2, RATE_WIDTH , RATE_HEIGHT , null);
					}
					//���ư���
					if (board[i][j] == 2)
					{
						g.drawImage(white, i * RATE_WIDTH + X_OFFSET - RATE_WIDTH / 2, j * RATE_HEIGHT + Y_OFFSET - RATE_HEIGHT / 2, RATE_WIDTH , RATE_HEIGHT , null);
					}
				}
			}
			if (RESULT == 1)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("����", Font.BOLD, 20));
				g.drawString("����ȡ��ʤ��������", TABLE_WIDTH / 2 - 70, 20);
			}
			else if (RESULT == 2)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("����", Font.BOLD, 20));
				g.drawString("����ȡ��ʤ��������", TABLE_WIDTH / 2 - 70, 20);
			}
			else if (RESULT == 3)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("����", Font.BOLD, 20));
				g.drawString("ƽ�֣�����", TABLE_WIDTH / 2 - 40, 30);
			}
		}
	}
}
