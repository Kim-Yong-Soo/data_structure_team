package data_structure_team;

class Station {
	String name;
	Station preLink;
	Station nextLink;

	public Station(Station preLink, String name, Station nextLink) {
		this.name = name;
		this.preLink = preLink;
		this.nextLink = nextLink;
	}
}

class StationList {
	Station head;

	public boolean search(String x) { // x역이 존재하는지를 boolean 값으로 반환한다.
		Station p = head;

		do {
			if (p.name.equals(x))
				return true;
			p = p.nextLink;
		} while (p.nextLink != head);

		return false;
	}

	public Station findStationNode(String x) { // x역이 존재하는지 확인하고 존재 시 해당 노드를, 존재하지 않을 경우 null값을 반환한다.
		Station p = head;

		do {
			if (p.name.equals(x))
				return p;
			p = p.nextLink;
		} while (p.nextLink != head);

		return null;
	}

	public void addFirst(String x) { // 노드의 맨 앞에 x역을 추가한다.
		Station newNode = new Station(null, x, null);

		if (head == null) {
			head = newNode;
			newNode.preLink = newNode;
			newNode.nextLink = newNode;
		} else {
			newNode.preLink = head.preLink;
			newNode.nextLink = head;
			head.preLink.nextLink = newNode;
			head.preLink = newNode;
			head = newNode;
		}
	}

	public void addMid(String x, String preStation, String nextStation) { // preStation과 nextStation 사이에 x역을 추가한다.
		Station preNode = findStationNode(preStation);
		Station nextNode = findStationNode(nextStation);

		if (head == null) {
			Station newNode = new Station(null, x, null);
			newNode.preLink = newNode;
			newNode.nextLink = newNode;
			head = newNode;
		} else {
			Station newNode = new Station(preNode, x, nextNode);
			newNode.preLink = preNode;
			newNode.nextLink = nextNode;
			preNode.nextLink = newNode;
			nextNode.preLink = newNode;
		}
	}

	public void addLast(String x) { // 노드의 맨 뒤에 x역을 추가한다.
		Station newNode = new Station(null, x, null);

		if (head == null) {
			newNode.preLink = newNode;
			newNode.nextLink = newNode;
			head = newNode;
		} else {
			newNode.preLink = head.preLink;
			newNode.nextLink = head;
			head.preLink.nextLink = newNode;
			head.preLink = newNode;
		}
	}

	public void print() { // 현재 2호선 역을 일렬로 출력한다. 추가로 역의 갯수도 구해 출력한다.
		Station temp = head;
		int cnt = 0;

		if (head == null) {
			System.out.println("등록되어 있는 정거장이 존재하지 않습니다.");
			return;
		}

		do {
			cnt++;
			System.out.print(cnt + " " + temp.name + " ");
			temp = temp.nextLink;
		} while (temp != head);

		System.out.println(" / 2호선 역의 갯수는 " + cnt + "개");
	}

	public int count() { // 2호선 역의 갯수를 출력한다.
		Station temp = head;
		int cnt = 0;

		if (head == null)
			return 0;

		do {
			cnt++;
			temp = temp.nextLink;
		} while (temp != head);

		return cnt;
	}

	public void delete(String x) { // x역을 찾아 노드의 연결을 끊어 삭제한다.
		Station p;
		p = head;

		while (head != null) {
			if (p.name.equals(x)) {
				p.preLink.nextLink = p.nextLink;
				p.nextLink.preLink = p.preLink;

				if (p == head)
					head = head.nextLink;
				p = null;
				return;
			}
			p = p.nextLink;
			if (p == head)
				break;
		}
	}

	public int minStation(String departure, String destination) { // departure에서 destination까지의 최소 정류장 거리를 구해 반환한다.
		if (head == null)
			return -1;

		Station start = findStationNode(departure);
		Station startP = findStationNode(departure);
		Station startQ = findStationNode(departure);
		int cnt = 0;

		if (departure.equals(destination))
			return 0;
		if (!search(departure) || !search(destination))
			return -1;

		while (startP.nextLink != start && startQ.preLink != start) {
			if (startP.name.equals(destination) || startQ.name.equals(destination))
				break;
			cnt++;
			startP = startP.nextLink;
			startQ = startQ.preLink;
		}

		return cnt;
	}

	public String docking() {
		// head역인 "잠실"에서 양쪽으로 출발한 열차가 어느 정거장에서 만나는지 구해 출력한다.
		// 혹시 역이 짝수여서 생기는 오버랩의 경우 오버랩되는 두 역 모두 출력한다.
		Station startP, startQ;
		String res = "";

		if (head == null)
			return "등록되어 있는 정거장이 존재하지 않습니다.";

		startP = head;
		startQ = head;
		while (startP.nextLink != head) {
			if (startP.nextLink == startQ.preLink)
				res = startP.nextLink.name + "역에서 만납니다.";
			else if (startP.nextLink == startQ && startQ.preLink == startP)
				res = startP.nextLink.name + "역, " + startQ.preLink.name + "역에서 서로 오버랩됩니다.";
			startP = startP.nextLink;
			startQ = startQ.preLink;
		}

		return res;
	}
}

public class Subway1 {

	public static void main(String[] args) {
		StationList stl = new StationList();

		// 구현 - 1
		stl.addFirst("잠실");
		stl.addLast("신천");
		stl.addLast("종합운동장");
		stl.addLast("삼성");
		stl.addLast("선릉");
		stl.addLast("역삼");
		stl.addLast("강남");
		stl.addLast("교대");
		stl.addLast("서초");
		stl.addLast("방배");
		stl.addLast("사당");
		stl.addLast("낙성대");
		stl.addLast("서울대입구");
		stl.addLast("봉천");
		stl.addLast("신림");
		stl.addLast("신대방");
		stl.addLast("구로디지털단지");
		stl.addLast("대림");
		stl.addLast("신도림");
		stl.addLast("문래");
		stl.addLast("영등포구청");
		stl.addLast("당산");
		stl.addLast("합정");
		stl.addLast("홍대입구");
		stl.addLast("신촌");
		stl.addLast("이대");
		stl.addLast("아현");
		stl.addLast("충정로");
		stl.addLast("시청");
		stl.addLast("을지로입구");
		stl.addLast("을지로3가");
		stl.addLast("을지로4가");
		stl.addLast("동대문역사문화공원");
		stl.addLast("신당");
		stl.addLast("상왕십리");
		stl.addLast("왕십리");
		stl.addLast("한양대");
		stl.addLast("뚝섬");
		stl.addLast("성수");
		stl.addLast("건대입구");
		stl.addLast("구의");
		stl.addLast("강변");
		stl.addLast("잠실나루");
		stl.print();

		// 잠실역 제거 - 1
		stl.delete("잠실");
		stl.print();

		// 왕십리에서 잠실까지 최소 정거장 - 2
		// stl.addFirst("잠실"); // 위에서 잠실역을 제거하였기 때문에 잠실역이 존재하지 않아 추가하여 확인한다.
		int minRes = stl.minStation("왕십리", "잠실");
		System.out.println(((minRes != -1) ? "왕십리에서 잠실까지 최소 " + minRes + " 정거장" : "잠실역이 존재하지 않습니다."));

		// 신천쪽, 강변쪽으로 잠실 출발 / 어느 역에서 만나는지? - 3
		System.out.println(stl.docking());

		// 정거장의 숫자는 몇개인지? - 4
		System.out.println("2호선 역의 갯수는 " + stl.count() + "개입니다.");

		// 방배역과 사당역 사이 별당역 추가 - 4
		stl.addMid("별당", "방배", "사당");
		stl.print();
	}

}