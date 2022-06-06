package data_structure_team;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class Employee {
	// 직원
	public int uniqueNum;
	private boolean isReady; // false: 조리 중, true: 준비 중
	private Order curOrder;
	private int waitingTime;

	public Employee(int uniqueNum) {
		this.uniqueNum = uniqueNum;
		isReady = true;
		curOrder = null;
		waitingTime = 0;
	}

	public boolean getIsReady() {
		return isReady;
	}

	public Order isDone(int time) {
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

	public void cook(Order curOrder, int curCookStartTime) {
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

class Customer {
	// 고객
	private int customerNum;

	public Customer(int customerNum) {
		this.customerNum = customerNum;
	}

	public int getCustomerNum() {
		return customerNum;
	}
}

class Order {
	// 주문 큐
	private int orderTime;
	private Customer orderCustomer;
	private Employee employee;
	private Menu menu;
	private int startTime;
	private int doneTime;

	public Order(Customer orderCustomer, int orderTime) {
		Menu m = new Menu();
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

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public Menu getMenu() {
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

class Menu {
	// 메뉴
	private int makingTime;
	private String drinkName;

	public Menu() {
	}

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

class Process {
	private Queue<Order> orderQueue;
	private Queue<Order> doneOrderQueue;
	private Employee employees[];
	private int time;
	private int currentCustomerNum;

	public Process() {
		orderQueue = new LinkedList<>();
		doneOrderQueue = new LinkedList<>();
		employees = new Employee[] { new Employee(1), new Employee(2) };
		time = 0;
		currentCustomerNum = 0;
	}

	public void simul() {
		while (time++ < 60) {
			Random random = new Random();
			int num = random.nextInt(100);
			if (num < 30)
				orderQueue.add(new Order(new Customer(++currentCustomerNum), time));
			cookAndCheck();
		}
	}

	public void cookAndCheck() {
		for (int i = 0; i < employees.length; i++) {
			if (employees[i].getIsReady()) {
				if (!orderQueue.isEmpty())
					employees[i].cook(orderQueue.poll(), time);
				else
					employees[i].setWaitingTime();
			}

			Order hasdoneOrder = employees[i].isDone(time);
			if (hasdoneOrder != null)
				doneOrderQueue.add(hasdoneOrder);
		}
	}

	public void donePrint() {
		if (doneOrderQueue.isEmpty()) {
			System.out.println("완료된 주문이 없습니다.");
			return;
		}
		int waitingTime = 0;
		Order firstOrder = doneOrderQueue.peek();
		do {
			waitingTime += doneOrderQueue.peek().getWaitingTime();
			System.out.println(doneOrderQueue.peek().toString());
			doneOrderQueue.add(doneOrderQueue.poll());
		} while (doneOrderQueue.peek() != firstOrder);
		System.out.println("고객님들의 평균 대기 시간은 " + String.format("%.2f분", (float) waitingTime / doneOrderQueue.size())
				+ "입니다. 총 대기 시간: " + waitingTime + ", 총 주문 수: " + doneOrderQueue.size());
	}

	public void employeeWaitTimePrint() {
		for (Employee e : employees)
			System.out.println("직원 " + e.uniqueNum + "의 주문 대기 시간은 " + e.getWaitingTime() + "분입니다.");
	}

}

public class HallymCafe_1 {

	public static void main(String[] args) {
		Process process = new Process();
		process.simul();
		process.donePrint();
		process.employeeWaitTimePrint();
	}

}
