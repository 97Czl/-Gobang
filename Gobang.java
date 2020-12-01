import java.util.*;
import java.util.regex.Pattern;

public class Gobang
{
	private final int size = 20;
	//��������
	private String[][] board = new String[size + 1][size + 1];

	public int AllNumber()
	{
		return size*size;
	}

	/**
	 *��ʼ�����̣���ÿһ��λ�ö���ʾ+ ����ʾ����
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
					board[i][j] = (j == 0 ? (i < 10 ? (" " + i) : ("" + i)) : "ʮ");
				}
			}
		}
		this.drawBoard();
	}

	/**
	 *����ǰ�����̻����
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
	 *�������ӵķ���
	 *@param place �������ӵ������ַ��� ������2��3
	 *@param order ������ӺͰ��ӵ�����trueΪ���ӣ�falseΪ����
	 *@return �����Ƿ����ӳɹ��Լ��Ƿ�ʤ����0��������ʧ�ܣ�1�������ӳɹ������ʤ����2�������ӳɹ��ֵ���һ������
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
			System.out.println("�����ʽ����ȷ ������ ����������  ��ʽ���������������ӣ�");
			return 0;
		}
		
		//��������ַ�������������ֵ��ȡ����
		String[] placeArray = place.split(",");
		location[0] = Integer.parseInt(placeArray[0]);
		location[1] = Integer.parseInt(placeArray[1]);
		
		if (location[0] < 1 || location[0] > size || location[1] < 1 || location[1] > size)
		{
			System.out.println("��λ�ò��ܷ�������");
			return 0;
		}
		else if (!board[location[0]][location[1]].equals("ʮ"))
		{
			System.out.println("��λ���Ѿ���������");
			return 0;
		}
		else  
			board[location[0]][location[1]] = (order == true) ? "��" : "��";

		//�ж��Ƿ��Ѿ�����
		if (numOfChess == (AllNumber() - 1))
		{
			drawBoard();
			System.out.println("�����Ѿ���������λ���������֣�����");
			return 1;
		}
		
		//�ж���Ϸ��ǰ״̬
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
	 *�жϵ�ǰ����ʱ����ʤ��
	 *@param place �����ʱ���ӵ�����
	 *@param order ������ӺͰ��ӵ�����trueΪ���ӣ�falseΪ����
	 *@return �����Ƿ���ʤ����true����ʤ����false����δʤ�� 
	 */
	private boolean ifVictory(int[] place, boolean order)
	{
		//�˴����ӵ��к�������
		var row = place[0];
		var column = place[1];

		//�����ĸ������ϵ�����
		String chessInColumn = "";
		String chessInrow = "";
		String chessInUp = "";
		String chessInDown = "";

		//�ɹ��İ��������������ͬ����
		String chessInVictory = board[row][column] + board[row][column] + board[row][column] + board[row][column] + board[row][column];

		//�жϸõ��������ĸ������Ƿ񹹳�����������
		//���жϸ���
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
			System.out.println("" + (order ? "��" : "��") + "����һ�ʤ��������ϲŶ����");
			return true;
		}
		else 
		{
			System.out.println("�������ֵ�" + (order ? "��" : "��") + "���������");
			return false;
		}
	}


	public static void main(String[] args) 
	{
		Gobang gb = new Gobang();
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome����ӭʹ�ñ�����������Ϸ������");
		System.out.println("������Ϸ��˫����Ϸ���������С��鿪ʼ��Ϸ�ɣ�");
		System.out.println("����Ϸ;�� �������ʱ ���� exit ���˳�");
		while (true)
		{
			gb.InitBoard();
			System.out.println("������ ����������  ��ʽ���������������ӣ�----------��������");

			//���� result δ��ǰ��Ϸ���Ӻ��״̬
			//���� numOfChess ��¼�������Ӹ�������ֹ����
			//�����Ⱥ��ֵ��ж�
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
			System.out.println("��Ϸ���������� next ������һ�ѣ� exit �˳���Ϸ");
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
				System.out.println("�����ʽ����ȷ�����˳���Ϸ������");
				break;
			}
			
		}
	}
}
