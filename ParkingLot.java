package data_structure_team;

class Car {
	int carNum;

	public Car(int carNum) {
		this.carNum = carNum;
	}
}

interface Stack {
	boolean isEmpty();

	void push(Car item);

	Car pop();

	void delete();

	Car peek();
}

class ArrayStackFunc implements Stack {
	private int top; // 방 번호
	private int stackSize = 10;
	private Car itemArray[];

	public ArrayStackFunc() {
		top = 0;
		itemArray = new Car[stackSize + 1];
	}

	public boolean isEmpty() {
		return (top == 0);
	}

	public boolean isFull() {
		return (top == stackSize);
	}

	public void push(Car item) {
		if (isFull()) {
			System.out.println("주차장이 만차입니다. 다른 주차장을 이용해주세요.");
			return;
		}
		top++;
		itemArray[top] = item;
	}

	public Car pop() {
		if (isEmpty()) {
			System.out.println("주차장이 비어있습니다. 주차장을 다시 확인해주세요.");
			return null;
		}

		return itemArray[top--];
	}

	public void delete() {
		if (isEmpty()) {
			System.out.println("주차장이 비어있습니다. 주차장을 다시 확인해주세요.");
			return;
		}
		top--;
	}

	public Car peek() {
		if (isEmpty()) {
			System.out.println("주차장이 비어있습니다. 주차장을 다시 확인해주세요.");
			return null;
		}
		return itemArray[top];
	}

	public int searchCarLoc(int carNum) {
		for (int i = 1; i <= stackSize; i++) {
			if (this.itemArray[i].carNum == carNum) {
				return i;
			}
		}
		return -1;
	}

	public int parkingCars() {
		return top;
	}

	public void process(int carNum) {
		int loc = searchCarLoc(carNum);

		if (loc == -1) {
			System.out.println("주차되어 있지 않은 차입니다.");
			return;
		}

		ArrayStackFunc parkingTmp = new ArrayStackFunc();

		while (top != loc) {
			parkingTmp.push(pop());
		}

		System.out.println("오래 기다리셨습니다. " + itemArray[top--].carNum + " 고객님 안녕히 가십시요.");

		while (!parkingTmp.isEmpty()) {
			push(parkingTmp.pop());
		}
	}

}

public class ParkingLot {

	public static void main(String[] args) {
		ArrayStackFunc parking1 = new ArrayStackFunc();

		parking1.push(new Car(1111));
		parking1.push(new Car(2222));
		parking1.push(new Car(3333));
		parking1.push(new Car(4444));
		parking1.push(new Car(5555));
		parking1.push(new Car(6666));
		parking1.push(new Car(7777));
		parking1.push(new Car(8888));
		parking1.push(new Car(9999));
		parking1.push(new Car(1010));

		parking1.process(4444);

		System.out.println(parking1.parkingCars());
	}

}
