package data_structure_team;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

class Employee2 {
	// 직원
	public int uniqueNum;
	private boolean isReady; // false: 조리 중, true: 준비 중
	private Order2 curOrder;
	private int waitingTime;

	public Employee2(int uniqueNum) {
		this.uniqueNum = uniqueNum;
		isReady = true;
		curOrder = null;
		waitingTime = 0;
	}

	public boolean getIsReady() {
		return isReady;
	}

	public Order2 isDone(int time) {
		if (!isReady) {
			int startTime = curOrder.getStartTime();
			if (startTime + curOrder.getMenu().getMakingTime() == time) {
				curOrder.setDoneTime(time);
				isReady = true;
				return curOrder;
			}
		}
		return null;
	}

	public void cook(Order2 curOrder, int curCookStartTime) {
		if (getIsReady()) {
			this.curOrder = curOrder;
			this.curOrder.setEmployee(this);
			this.curOrder.setStartTime(curCookStartTime);
			isReady = false;
		}
	}

	public void setWaitingTime() {
		waitingTime++;
	}

	public int getWaitingTime() {
		return waitingTime;
	}
}

class Customer3 {
	// 고객
	private int customerNum;

	public Customer3(int customerNum) {
		this.customerNum = customerNum;
	}

	public int getCustomerNum() {
		return customerNum;
	}
}

class Order2 {
	// 주문 큐
	private int orderTime;
	private Customer3 orderCustomer;
	private Employee2 employee;
	private Menu2 menu;
	private int startTime;
	private int doneTime;

	public Order2(Customer3 orderCustomer, int orderTime) {
		Menu2 m = new Menu2();
		m.chooseMenu();
		menu = m;
		this.orderCustomer = orderCustomer;
		this.orderTime = orderTime;
		employee = null;
		startTime = 0;
		doneTime = 0;
	}

	public void setDoneTime(int doneTime) {
		this.doneTime = doneTime;
	}

	public void setEmployee(Employee2 employee) {
		this.employee = employee;
	}

	public Employee2 getEmployee() {
		return employee;
	}

	public Menu2 getMenu() {
		return menu;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getWaitingTime() {
		return doneTime - orderTime;
	}

	@Override
	public String toString() {
		return String.format("%3d", orderCustomer.getCustomerNum()) + "번째 고객님께서 " + String.format("%2d", orderTime)
				+ "분에 가게에 입장하셨고, " + String.format("%2d", startTime) + "분에 " + String.format("%6s", menu.getDrinkName())
				+ "를 주문을 받고 조리가 시작되었습니다. 그리고 " + String.format("%2d", doneTime) + "분에 조리가 완료되어 제공되었습니다. / 담당 직원: "
				+ String.format("%2d", employee.uniqueNum);
	}

}

class Menu2 {
	// 메뉴
	private int makingTime;
	private String drinkName;

	public void chooseMenu() {
		Random random = new Random();
		int num = random.nextInt(100);

		makingTime = (num < 40) ? 5 : (num < 70) ? 4 : (num < 90) ? 2 : 1;
		drinkName = (num < 40) ? "카페라떼" : (num < 70) ? "아메리카노" : (num < 90) ? "오렌지주스" : "콜라";
	}

	public int getMakingTime() {
		return makingTime;
	}

	public String getDrinkName() {
		return drinkName;
	}
}

class Process2 {
	private PriorityQueue<Order2> orderQueue;
	private Queue<Order2> doneOrderQueue;
	private Employee2 employees[];
	private int time;
	private int currentCustomerNum;

	public Process2() {
		orderQueue = new PriorityQueue<>(60, new Comparator<Order2>() {

			@Override
			public int compare(Order2 o1, Order2 o2) {
				if (o1.getMenu().getMakingTime() > o1.getMenu().getMakingTime())
					return -1;
				else if (o1.getMenu().getMakingTime() < o1.getMenu().getMakingTime())
					return 1;
				return 0;
			}

		});
		doneOrderQueue = new LinkedList<>();
		employees = new Employee2[] { new Employee2(1), new Employee2(2) };
		time = 0;
		currentCustomerNum = 0;
	}

	public void simul() {
		while (time++ < 60) {
			Random random = new Random();
			int num = random.nextInt(100);
			if (num < 30)
				orderQueue.add(new Order2(new Customer3(++currentCustomerNum), time));
			cookAndCheck();
		}
	}

	public void cookAndCheck() {
		for (int i = 0; i < employees.length; i++) {
			Order2 hasdoneOrder = employees[i].isDone(time);
			if (hasdoneOrder != null)
				doneOrderQueue.add(hasdoneOrder);
			
			if (employees[i].getIsReady()) {
				if (!orderQueue.isEmpty())
					employees[i].cook(orderQueue.poll(), time);
				else
					employees[i].setWaitingTime();
			}
		}
	}

	public void donePrint() {
		if (doneOrderQueue.isEmpty()) {
			System.out.println("완료된 주문이 없습니다.");
			return;
		}
		int waitingTime = 0, maxWaitingTime = doneOrderQueue.peek().getWaitingTime();
		Order2 firstOrder = doneOrderQueue.peek();
		do {
			if (maxWaitingTime < doneOrderQueue.peek().getWaitingTime())
				maxWaitingTime = doneOrderQueue.peek().getWaitingTime();
			waitingTime += doneOrderQueue.peek().getWaitingTime();
			System.out.println(doneOrderQueue.peek().toString());
			doneOrderQueue.add(doneOrderQueue.poll());
		} while (doneOrderQueue.peek() != firstOrder);
		System.out.println("가장 오래 기다리신 고객님의 대기 시간은 " + maxWaitingTime + "분입니다.");
		System.out.println("고객님들의 평균 대기 시간은 " + String.format("%.2f분", (float) waitingTime / doneOrderQueue.size())
				+ "입니다. 총 대기 시간: " + waitingTime + ", 총 주문 수: " + doneOrderQueue.size());
	}

	public void employeeWaitTimePrint() {
		for (Employee2 e : employees)
			System.out.println("직원 " + e.uniqueNum + "의 주문 대기 시간은 " + e.getWaitingTime() + "분입니다.");
	}

}

public class HallymCafe_2 {

	public static void main(String[] args) {
		Process2 process = new Process2();
		process.simul();
		process.donePrint();
		process.employeeWaitTimePrint();
	}

}