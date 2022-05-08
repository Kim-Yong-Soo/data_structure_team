package data_structure_team;

import java.util.Stack;

class Piece {
	int row; // 행
	int col; // 열
	int dir; // 방향

	public Piece(int row, int col, int dir) {
		this.row = row;
		this.col = col;
		this.dir = dir; // 0:북, 1:동, 2:남, 3:서
	}
}

interface PieceStack {
	boolean isEmpty(); // 스택이 비어 있는가?

	boolean isFull(); // 스택이 가득 찼는가?

	void push(Piece piece); // Piece 객체 삽입

	Piece pop(); // 맨 위의 Piece 객체를 반환 및 삭제

	Piece peek(); // 맨 위의 Piece 객체를 반환
}

class SolveMaze implements PieceStack { // 직접 구현한 배열을 이용한 Stack을 사용한 풀이 클래스
	private int top; // 스택 맨 위 객체의 위치
	private int maze[][], mark[][]; // maze: 클래스 내 미로 배열, mark: 클래스 내 이동 여부 배열
	private Piece pieceArray[]; // 이동 경로 스택

	public SolveMaze(int inMaze[][]) {
		top = -1;
		this.maze = new int[inMaze.length + 2][inMaze[0].length + 2]; // 벽을 추가하기 위해 사방면으로 각각 +1
		this.mark = new int[inMaze.length + 2][inMaze[0].length + 2]; // 벽을 추가하기 위해 사방면으로 각각 +1

		for (int i = 0; i < this.maze[0].length; i++) { // 맨 위, 아래 벽(1)으로 설정
			this.maze[0][i] = 1;
			this.maze[this.maze.length - 1][i] = 1;
		}

		for (int i = 1; i <= inMaze.length; i++) // 벽 안에 입력된 미로를 추가
			for (int j = 1; j <= inMaze[0].length; j++)
				this.maze[i][j] = inMaze[i - 1][j - 1];

		for (int i = 0; i < this.maze.length; i++) { // 맨 앞, 뒤 벽(1)으로 설정
			this.maze[i][0] = 1;
			this.maze[i][this.maze[0].length - 1] = 1;
		}
	}

	// 0:북, 1:동, 2:남, 3:서
	public void process() {
		pieceArray = new Piece[(maze.length - 2) * (maze.length - 2)]; // 스택으로 사용할 배열 생성
		push(new Piece(1, 1, 1)); // 초기 위치 삽입
		mark[1][1] = 1; // 초기 위치 이동 여부 추가

		printMaze();
		System.out.println();

		while (!isEmpty()) { // 스택이 비어 있지 않다면 반복
			Piece tmp = pop(); // 현재 위치를 반환 받음
			int dir = tmp.dir; // 반복문 내에서 변경하며 사용하기 위해 따로 변수로 지정
			int movCnt = 0;

			while (dir <= 3) { // 방향이 서쪽일때까지만
				int nex_row = tmp.row + move(dir, 0); // 다음 행 설정
				int nex_col = tmp.col + move(dir, 1); // 다음 열 설정

				if (nex_row == maze.length - 2 && nex_col == maze[0].length - 1) {
					// 만약 끝에 도착했을때 종료
					// -2와 -1로 다른 이유는 밑의 조건문을 실행해야 탈출 경로가 지정되기 때문에 반복문을 한 번 더 돌리기 위해
					System.out.println("미로 탈출 경로");
					tmp.dir = 1; // 탈출은 동쪽으로 되는 걸로 설정
					push(tmp); // 현재 위치를 다시 push()
					pathPrintAsEmoji(); // 미로 탈출 경로 출력
					return;
				}

				if (maze[nex_row][nex_col] == 0 && mark[nex_row][nex_col] == 0) { // 만약 미로의 벽이 아니고, 가지 않은 경로라면 이동
					mark[nex_row][nex_col] = 1; // 다음 위치를 가본 경로로 값 변경
					tmp.dir = dir; // 현재 위치의 방향을 다음 위치를 바라보도록 방향 설정
					push(tmp); // 현재 위치를 다시 push()
					push(new Piece(nex_row, nex_col, dir)); // 다음 위치를 push()
					break;
				} else {
					if (tmp.dir != 0 && dir == 3) // 북쪽에서 시작되지 않은 경우 북쪽까지 돌아볼 수 있도록 설정
						dir = 0;
					else if (movCnt == 3) // 사방면을 모두 확인한 경우 탈출
						break;
					else
						dir++; // 다음 방향
				}
				movCnt++; // 이동 횟수 추가
			}
		}

		// 경로가 존재하지 않는 경우 종료
		System.out.println("미로 탈출 경로가 존재하지 않습니다");
	}

	public int move(int dir, int pos) { // dir: 입력된 현재 방향, pos: 0은 행, 1은 열
		if (pos == 0) { // 행일 경우
			switch (dir) {
			case 0: // 북쪽일 경우 -1
				return -1;
			case 2: // 남쪽일 경우 +1
				return 1;
			default: // 나머지는 행의 이동을 하지 않음
				return 0;
			}
		} else if (pos == 1) {
			switch (dir) {
			case 3: // 서쪽일 경우 -1
				return -1;
			case 1: // 동쪽일 경우 +1
				return 1;
			default: // 나머지는 열의 이동을 하지 않음
				return 0;
			}
		}
		return 0;
	}

	public void pathPrint() { // 0과 1로만 탈출 경로를 출력하는 함수
		int tmp[][] = new int[mark.length - 2][mark[0].length - 2]; // 실제 미로 사이즈의 배열 생성
		while (!isEmpty()) { // 모든 탈출 piece를 불러와 각 위치에 1을 배열에 입력
			Piece piece = pop();
			tmp[piece.row - 1][piece.col - 1] = 1;
		}

		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[0].length; j++)
				System.out.printf("%2d", tmp[i][j]); // 0과 1로 출력
			System.out.println();
		}
	}

	public void pathPrintAsEmoji() { // 이모지로 탈출 경로를 출력하는 함수
		int tmp[][] = new int[mark.length - 2][mark[0].length - 2]; // 실제 미로 사이즈의 배열 생성
		while (!isEmpty()) { // 모든 탈출 piece를 불러와 각 위치에 방향을 배열에 입력
			Piece piece = pop();
			tmp[piece.row - 1][piece.col - 1] = piece.dir + 1;
		}

		for (int i = 0; i < tmp.length; i++) {
			// 실제 미로에서 벽이 아니고 탈출 경로가 아닌 경우는 □를 출력, 다른 경우에는 이모지로 출력
			for (int j = 0; j < tmp[0].length; j++)
				System.out.printf("%2s", (maze[i + 1][j + 1] == 0 && tmp[i][j] == 0) ? "□" : selectEmoji(tmp[i][j]));
			System.out.println();
		}
	}

	public char selectEmoji(int curDir) { // 방향별로 이모지 선택
		// 1: 북, 2: 동, 3: 남, 4: 서
		switch (curDir) {
		case 1:
			return '↑';
		case 2:
			return '→';
		case 3:
			return '↓';
		case 4:
			return '←';
		default:
			return '■'; // 벽의 경우
		}
	}

	public void printMaze() { // 클래스 내의 미로 출력
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++)
				System.out.printf("%2s", (maze[i][j] == 1) ? '■' : '□');
			System.out.println();
		}
	}

	public boolean isEmpty() { // 스택이 비었는가?
		return (top == -1);
	}

	public boolean isFull() { // 스택이 가득 찼는가?
		return (top == pieceArray.length - 1);
	}

	public void push(Piece piece) { // Piece 객체 삽입
		if (isFull()) {
			System.out.println("push - isFull()");
			return;
		}
		top++;
		pieceArray[top] = piece;
	}

	public Piece pop() { // 맨 위의 Piece 객체를 반환 및 삭제
		if (isEmpty()) {
			System.out.println("pop - isEmpty()");
			return null;
		}
		return pieceArray[top--];
	}

	public Piece peek() { // 맨 위의 Piece 객체를 반환
		if (isEmpty()) {
			System.out.println("peek - isEmpty()");
			return null;
		}
		return pieceArray[top];
	}
}

class SolveMazeUtil { // java.util.Stack을 사용한 풀이 클래스
	private int maze[][], mark[][]; // maze: 클래스 내 미로 배열, mark: 클래스 내 이동 여부 배열
	private Stack<Piece> pieceStack;

	public SolveMazeUtil(int inMaze[][]) {
		this.maze = new int[inMaze.length + 2][inMaze[0].length + 2]; // 벽을 추가하기 위해 사방면으로 각각 +1
		this.mark = new int[inMaze.length + 2][inMaze[0].length + 2]; // 벽을 추가하기 위해 사방면으로 각각 +1

		for (int i = 0; i < this.maze[0].length; i++) { // 맨 위, 아래 벽(1)으로 설정
			this.maze[0][i] = 1;
			this.maze[this.maze.length - 1][i] = 1;
		}

		for (int i = 1; i <= inMaze.length; i++) // 벽 안에 입력된 미로를 추가
			for (int j = 1; j <= inMaze[0].length; j++)
				this.maze[i][j] = inMaze[i - 1][j - 1];

		for (int i = 0; i < this.maze.length; i++) { // 맨 앞, 뒤 벽(1)으로 설정
			this.maze[i][0] = 1;
			this.maze[i][this.maze[0].length - 1] = 1;
		}
	}

	// 0:북, 1:동, 2:남, 3:서
	public void process() {
		pieceStack = new Stack<>(); // 스택 생성
		pieceStack.push(new Piece(1, 1, 1)); // 초기 위치 삽입
		mark[1][1] = 1; // 초기 위치 이동 여부 추가

		printMaze();
		System.out.println();

		while (!pieceStack.isEmpty()) { // 스택이 비어 있지 않다면 반복
			Piece tmp = pieceStack.pop(); // 현재 위치를 반환 받음
			int dir = tmp.dir; // 반복문 내에서 변경하며 사용하기 위해 따로 변수로 지정
			int movCnt = 0;

			while (dir <= 3) { // 방향이 서쪽일때까지만
				int nex_row = tmp.row + move(dir, 0); // 다음 행 설정
				int nex_col = tmp.col + move(dir, 1); // 다음 열 설정

				if (nex_row == maze.length - 2 && nex_col == maze[0].length - 1) {
					// 만약 끝에 도착했을때 종료
					// -2와 -1로 다른 이유는 밑의 조건문을 실행해야 탈출 경로가 지정되기 때문에 반복문을 한 번 더 돌리기 위해
					System.out.println("미로 탈출 경로");
					tmp.dir = 1; // 탈출은 동쪽으로 되는 걸로 설정
					pieceStack.push(tmp); // 현재 위치를 다시 push()
					pathPrintAsEmoji(); // 미로 탈출 경로 출력
					return;
				}

				if (maze[nex_row][nex_col] == 0 && mark[nex_row][nex_col] == 0) { // 만약 미로의 벽이 아니고, 가지 않은 경로라면 이동
					mark[nex_row][nex_col] = 1; // 다음 위치를 가본 경로로 값 변경
					tmp.dir = dir; // 현재 위치의 방향을 다음 위치를 바라보도록 방향 설정
					pieceStack.push(tmp); // 현재 위치를 다시 push()
					pieceStack.push(new Piece(nex_row, nex_col, dir)); // 다음 위치를 push()
					break;
				} else {
					if (tmp.dir != 0 && dir == 3) // 북쪽에서 시작되지 않은 경우 북쪽까지 돌아볼 수 있도록 설정
						dir = 0;
					else if (movCnt == 3) // 사방면을 모두 확인한 경우 탈출
						break;
					else
						dir++; // 다음 방향
				}
				movCnt++; // 이동 횟수 추가
			}
		}

		// 경로가 존재하지 않는 경우 종료
		System.out.println("미로 탈출 경로가 존재하지 않습니다");
	}

	public int move(int dir, int pos) { // dir: 입력된 현재 방향, pos: 0은 행, 1은 열
		if (pos == 0) { // 행일 경우
			switch (dir) {
			case 0: // 북쪽일 경우 -1
				return -1;
			case 2: // 남쪽일 경우 +1
				return 1;
			default: // 나머지는 행의 이동을 하지 않음
				return 0;
			}
		} else if (pos == 1) {
			switch (dir) {
			case 3: // 서쪽일 경우 -1
				return -1;
			case 1: // 동쪽일 경우 +1
				return 1;
			default: // 나머지는 열의 이동을 하지 않음
				return 0;
			}
		}
		return 0;
	}

	public void pathPrint() { // 0과 1로만 탈출 경로를 출력하는 함수
		int tmp[][] = new int[mark.length - 2][mark[0].length - 2]; // 실제 미로 사이즈의 배열 생성
		while (!pieceStack.isEmpty()) { // 모든 탈출 piece를 불러와 각 위치에 1을 배열에 입력
			Piece piece = pieceStack.pop();
			tmp[piece.row - 1][piece.col - 1] = 1;
		}

		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[0].length; j++)
				System.out.printf("%2d", tmp[i][j]); // 0과 1로 출력
			System.out.println();
		}
	}

	public void pathPrintAsEmoji() { // 이모지로 탈출 경로를 출력하는 함수
		int tmp[][] = new int[mark.length - 2][mark[0].length - 2]; // 실제 미로 사이즈의 배열 생성
		while (!pieceStack.isEmpty()) { // 모든 탈출 piece를 불러와 각 위치에 방향을 배열에 입력
			Piece piece = pieceStack.pop();
			tmp[piece.row - 1][piece.col - 1] = piece.dir + 1;
		}

		for (int i = 0; i < tmp.length; i++) {
			// 실제 미로에서 벽이 아니고 탈출 경로가 아닌 경우는 □를 출력, 다른 경우에는 이모지로 출력
			for (int j = 0; j < tmp[0].length; j++)
				System.out.printf("%2s", (maze[i + 1][j + 1] == 0 && tmp[i][j] == 0) ? "□" : selectEmoji(tmp[i][j]));
			System.out.println();
		}
	}

	public char selectEmoji(int curDir) { // 방향별로 이모지 선택
		// 1: 북, 2: 동, 3: 남, 4: 서
		switch (curDir) {
		case 1:
			return '↑';
		case 2:
			return '→';
		case 3:
			return '↓';
		case 4:
			return '←';
		default:
			return '■'; // 벽의 경우
		}
	}

	public void printMaze() { // 클래스 내의 미로 출력
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++)
				System.out.printf("%2s", (maze[i][j] == 1) ? '■' : '□');
			System.out.println();
		}
	}
}

public class Maze {

	public static void main(String[] args) {
		int m[][] = { { 0, 1, 1, 0, 1, 1 }, { 0, 0, 1, 0, 1, 1 }, { 1, 0, 0, 0, 1, 1 }, { 1, 1, 0, 0, 0, 1 },
				{ 0, 0, 0, 1, 1, 1 }, { 1, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0 } };

		SolveMaze sm = new SolveMaze(m); // SolveMaze 클래스
		sm.process(); // 과정 진행

//		SolveMazeUtil smu = new SolveMazeUtil(m); // SolveMazeUtil 클래스
//		smu.process(); // 과정 진행
	}

}
