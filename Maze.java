package data_structure_team;

class Piece {
	int row;
	int col;
	int dir;

	public Piece(int row, int col, int dir) {
		this.row = row;
		this.col = col;
		this.dir = dir;
	}
}

interface PeiceStack {
	boolean isEmpty();

	void push(Piece item);

	Piece pop();

	void delete();

	Piece peek();
}

class SolveMaze implements PeiceStack {
	private int top;
	private int maze[][], mark[][];
	private Piece itemArray[];

	public SolveMaze(int inMaze[][]) {
		top = -1;
		this.maze = new int[inMaze.length + 2][inMaze[0].length + 2];
		this.mark = new int[inMaze.length + 2][inMaze[0].length + 2];
		for (int i = 0; i < this.maze[0].length; i++) {
			this.maze[0][i] = 1;
			this.maze[this.maze.length - 1][i] = 1;
		}

		for (int i = 1; i <= inMaze.length; i++) {
			for (int j = 1; j <= inMaze[0].length; j++) {
				this.maze[i][j] = inMaze[i - 1][j - 1];
			}
		}

		for (int i = 0; i < this.maze.length; i++) {
			this.maze[i][0] = 1;
			this.maze[i][this.maze[0].length - 1] = 1;
		}
	}

	// 0:북, 1:동, 2:남, 3:서
	public void process() {
		itemArray = new Piece[(maze.length - 2) * (maze.length - 2)];
		push(new Piece(1, 1, 1));
		mark[1][1] = 1;

		printMaze();
		System.out.println();

		while (!isEmpty()) {
			Piece tmp = pop();

			while (tmp.dir <= 3) {
				int nex_row = tmp.row + move(tmp.dir, 0);
				int nex_col = tmp.col + move(tmp.dir, 1);
				// System.out.println("maze[" + nex_row + "][" + nex_col + "] = " +
				// maze[nex_row][nex_col] + ", " + tmp.dir);

				if (nex_row == maze.length - 2 && nex_col == maze[0].length - 1) {
					// System.out.println("Process Completed");
					push(tmp);
					// System.out.println(peek().row + ", " + peek().col);
					pathPrint();
					return;
				}

				if (maze[nex_row][nex_col] == 0 && mark[nex_row][nex_col] == 0) {
					// System.out.println("in");
					mark[nex_row][nex_col] = 1;
					tmp.dir = 0;
					push(tmp);
					push(new Piece(nex_row, nex_col, 0));
					// System.out.println("top: " + top);
					break;
				} else {
					tmp.dir++;
				}
			}
			// System.out.println("top: " + top);
			// System.out.println("Change!\n");
		}

		pathPrint();
		System.out.println("No path");
	}

	public int move(int dir, int pos) {
		if (pos == 0) {
			switch (dir) {
			case 0:
				return -1;
			case 2:
				return 1;
			default:
				return 0;
			}
		} else if (pos == 1) {
			switch (dir) {
			case 3:
				return -1;
			case 1:
				return 1;
			default:
				return 0;
			}
		}
		return 0;
	}

	public void pathPrint() {
		int tmp[][] = new int[mark.length - 2][mark[0].length - 2];
		while (!isEmpty()) {
			Piece piece = pop();
			tmp[piece.row - 1][piece.col - 1] = 1;
		}

		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[0].length; j++)
				System.out.printf("%2d", tmp[i][j]);
			System.out.println();
		}
	}

	public void printMaze() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length - 1; j++)
				System.out.printf("%2d", maze[i][j]);
			System.out.println();
		}
	}

	@Override
	public boolean isEmpty() {
		return (top == -1);
	}

	public boolean isFull() {
		return (top == itemArray.length - 1);
	}

	@Override
	public void push(Piece item) {
		if (isFull()) {
			System.out.println("push - isFull()");
			return;
		}
		top++;
		itemArray[top] = item;
	}

	@Override
	public Piece pop() {
		if (isEmpty()) {
			System.out.println("pop - isEmpty()");
			return null;
		}
		return itemArray[top--];
	}

	@Override
	public void delete() {
		if (isEmpty()) {
			System.out.println("delete - isEmpty()");
			return;
		}
		top--;
	}

	@Override
	public Piece peek() {
		if (isEmpty()) {
			System.out.println("peek - isEmpty()");
			return null;
		}
		return itemArray[top];
	}
}

public class Maze {

	public static void main(String[] args) {
		int m[][] = { { 0, 1, 1, 0, 1, 1 }, { 0, 0, 1, 0, 1, 1 }, { 1, 0, 0, 0, 1, 1 }, { 1, 1, 0, 0, 0, 1 },
				{ 0, 0, 0, 1, 1, 1 }, { 1, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0 } };
		SolveMaze sm = new SolveMaze(m);
		sm.process();
	}

}
