import java.util.*;
import java.util.regex.Pattern;

public class Gobang
{
	private final int size = 20;
	//定义棋盘
	private String[][] board = new String[size + 1][size + 1];

	public int AllNumber()
	{
		return size*size;
	}

	/**
	 *初始化棋盘，将每一个位置都显示+ 来表示棋盘
	 */ 
	public void InitBoard()
	{
		for (var i = 0;i < board.length; i++)
		{
			for (var j = 0;j < board[0].length; j++)
			{
				if (i == 0)
				{
					board[i][j] = (j == 0 ? "  " : (j < 10) ? (" " + j) : ("" + j));
				}
				else
				{
					board[i][j] = (j == 0 ? (i < 10 ? (" " + i) : ("" + i)) : "十");
				}
			}
		}
		this.drawBoard();
	}

	/**
	 *将当前的棋盘绘出来
	 */ 
	private void drawBoard()
	{
		for (var i = 0;i < board.length; i++)
		{
			for (var j = 0;j < board[0].length; j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 *放置棋子的方法
	 *@param place 放置棋子的坐标字符串 ，例如2，3
	 *@param order 代表黑子和白子的区别，true为黑子，false为白子
	 *@return 返回是否落子成功以及是否胜利，0代表落子失败，1代表落子成功并获得胜利，2代表落子成功轮到另一子落棋
	 */ 
	public int placeChess(String place, boolean order, int numOfChess)
	{
		int[] location = new int[2];
		Pattern locPattern = Pattern.compile("\\d{1,2},\\d{1,2}");
		try
		{
			if (! locPattern.matcher(place).matches())
			{
				throw new IllegalArgumentException();
			}
		}
		catch (IllegalArgumentException ie)
		{
			System.out.println("输入格式不正确 请输入 行数，列数  格式的坐标来放置棋子！");
			return 0;
		}
		
		//将输入的字符串的两个坐标值提取出来
		String[] placeArray = place.split(",");
		location[0] = Integer.parseInt(placeArray[0]);
		location[1] = Integer.parseInt(placeArray[1]);
		
		if (location[0] < 1 || location[0] > size || location[1] < 1 || location[1] > size)
		{
			System.out.println("该位置不能放置棋子");
			return 0;
		}
		else if (!board[location[0]][location[1]].equals("十"))
		{
			System.out.println("该位置已经有棋子了");
			return 0;
		}
		else  
			board[location[0]][location[1]] = (order == true) ? "○" : "●";

		//判断是否已经下满
		if (numOfChess == (AllNumber() - 1))
		{
			drawBoard();
			System.out.println("棋盘已经下满，二位真是棋逢对手！！！");
			return 1;
		}
		
		//判断游戏当前状态
		drawBoard();
		if (ifVictory(location, order))
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}

	/**
	 *判断当前落子时候获得胜利
	 *@param place 代表此时落子的坐标
	 *@param order 代表黑子和白子的区别，true为黑子，false为白子
	 *@return 返回是否获得胜利，true代表胜利，false代表未胜利 
	 */
	private boolean ifVictory(int[] place, boolean order)
	{
		//此次落子的行和列坐标
		var row = place[0];
		var column = place[1];

		//保存四个方向上的棋子
		String chessInColumn = "";
		String chessInrow = "";
		String chessInUp = "";
		String chessInDown = "";

		//成功的案例是连续五个相同棋子
		String chessInVictory = board[row][column] + board[row][column] + board[row][column] + board[row][column] + board[row][column];

		//判断该点所处的四个方向是否构成连续的五子
		//先判断该行
		for (var i = -4; i < 5; i++)
		{
			if ((row + i) < 1 || (row + i) > size )
			{}
			else 
			{
				chessInrow = chessInrow + board[row + i][column];
			}

			if ((column + i) < 1 || (column + i) > size)
			{}
			else
			{
				chessInColumn = chessInColumn + board[row][column + i];
			}

			for (var j = -4; j < 5; j++)
			{
				if (i != j)
				{
					continue;
				}
				if (((column + j) <= size && (column + j) > 0) && ((row - i) <= size && (row - i) > 0))
				{
					chessInUp = chessInUp + board[row - i][column + j];
				}
				if (((column + j) <= size && (column + j) > 0) && ((row + i) <= size && (row + i) > 0))
				{
					chessInDown = chessInDown + board[row + i][column + j];
				}
			}
		}
		if (chessInColumn.contains(chessInVictory) || chessInrow.contains(chessInVictory) || chessInUp.contains(chessInVictory) || chessInDown.contains(chessInVictory) )
		{
			System.out.println("" + (order ? "黑" : "白") + "子玩家获胜！！！恭喜哦！！");
			return true;
		}
		else 
		{
			System.out.println("接下来轮到" + (order ? "白" : "黑") + "子玩家下棋");
			return false;
		}
	}


	public static void main(String[] args) 
	{
		Gobang gb = new Gobang();
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome，欢迎使用本款五子棋游戏！！！");
		System.out.println("本款游戏是双人游戏，叫上你的小伙伴开始游戏吧！");
		System.out.println("在游戏途中 你可以随时 输入 exit 来退出");
		while (true)
		{
			gb.InitBoard();
			System.out.println("请输入 行数，列数  格式的坐标来放置棋子！----------黑子先行");

			//定义 result 未当前游戏落子后的状态
			//定义 numOfChess 记录棋盘棋子个数，防止下满
			//定义先后手的判断
			int result = 0;
			int numOfChess = 0;
			boolean order = true;

			while (true)
			{
				String location = sc.nextLine();
				if (location.equals("exit"))
				{
					return;
				}
				result = gb.placeChess(location, order, numOfChess);
				if (result == 0)
				{
					continue;
				}
				else if (result == 2)
				{
					order = ! order;
					numOfChess++;
					continue;
				}
				else if (result == 1)
				{
					break;
				}
			}
			System.out.println("游戏结束，输入 next 进行下一把， exit 退出游戏");
			try
			{
				String next = sc.nextLine();
				if (next.equals("exit"))
				{
					return;
				}
				else if (next.equals("next"))
				{
					continue;
				}
				else 
					throw new RuntimeException();
			}
			catch (RuntimeException re)
			{
				System.out.println("输入格式不正确，已退出游戏！！！");
				break;
			}
			
		}
	}
}
