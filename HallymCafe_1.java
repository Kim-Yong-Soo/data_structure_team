package data_structure_team;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class Employee {
	// 직원
	public int uniqueNum;
	private boolean isReady; // false: 조리 중, true: 준비 중
	private Order curOrder;
	private int curCookStartTime;

	public Employee(int uniqueNum) {
		this.uniqueNum = uniqueNum;
		isReady = true;
		curOrder = null;
		curCookStartTime = -1;
	}

	public boolean getIsReady() {
		return isReady;
	}

	public Order isDone(int time) {
		if (!isReady) {
			if (curCookStartTime + curOrder.getMenu().getMakingTime() == time) {
				curOrder.done(curCookStartTime, time);
				Order doneOrder = curOrder;
				isReady = true;
				curCookStartTime = -1;
				return doneOrder;
			}
		}
		return null;
	}

	public void cook(Order curOrder, int curCookStartTime) {
		if (getIsReady()) {
			this.curCookStartTime = curCookStartTime;
			this.curOrder = curOrder;
			this.curOrder.setEmployee(this);
			isReady = false;
		}
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

	public void done(int startTime, int doneTime) {
		this.startTime = startTime;
		this.doneTime = doneTime;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Menu getMenu() {
		return menu;
	}

	@Override
	public String toString() { // isDone 조건 추가하기
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
			if (num < 30) {
				orderQueue.add(new Order(new Customer(++currentCustomerNum), time));
			}
			cooking();
			checking();
		}

		while (!doneOrderQueue.isEmpty()) {
			System.out.println(doneOrderQueue.poll().toString());
		}
	}

	public void cooking() {
		for (int i = 0; i < employees.length; i++) {
			if (employees[i].getIsReady() && !orderQueue.isEmpty()) {
				employees[i].cook(orderQueue.poll(), time);
			}
		}
	}

	public void checking() {
		for (int i = 0; i < employees.length; i++) {
			Order hasdoneOrder = employees[i].isDone(time);
			if (hasdoneOrder != null) {
				doneOrderQueue.add(hasdoneOrder);
			}
		}
	}

}

public class HallymCafe_1 {

	public static void main(String[] args) {
		Process process = new Process();
		process.simul();

	}

}
