package data_structure_team;

import java.util.LinkedList;
import java.util.Queue;

public class FibQueue {

	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<>();
		int cnt = 0;

		queue.add(0);
		queue.add(1);

		do {
			int a = queue.remove();
			int b = queue.peek();
			queue.add(a + b);
			cnt++;
		} while (cnt != 10);
		
		queue.remove();
		System.out.println(queue.remove());
	}

}
