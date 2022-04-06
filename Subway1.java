package data_structure_team;

class Station {
	String name;
	Station preLink;
	Station nextLink;

	public Station(String name, Station preLink, Station nextLink) {
		this.name = name;
		this.preLink = preLink;
		this.nextLink = nextLink;
	}
}

class StationList {
	Station head;

	public Station findNode(String x) {
		Station p = head;
		do {
			if (p.name.equals(x))
				return p;
			p = p.nextLink;
		} while (p.nextLink != head);
		return null;
	}

	public void addFirst(String x) {
		Station newNode = new Station(x, null, null);
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

	public void addMid(String x, String preStation, String nextStation) {
		Station preNode = findNode(preStation);
		Station nextNode = findNode(nextStation);
		if (head == null) {
			Station newNode = new Station(x, null, null);
			newNode.preLink = newNode;
			newNode.nextLink = newNode;
			head = newNode;
		} else {
			Station newNode = new Station(x, preNode, nextNode);
			newNode.preLink = preNode;
			newNode.nextLink = nextNode;
			preNode.nextLink = newNode;
			nextNode.preLink = newNode;
		}
	}

	public void addLast(String x) {
		Station newNode = new Station(x, null, null);
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

	public void print() {
		Station temp = head;
		int cnt = 0;
		do {
			cnt++;
			System.out.print(cnt + " " + temp.name + " ");
			temp = temp.nextLink;
		} while (temp != head);
		System.out.println("2호선 역의 갯수는 " + cnt);
	}

	public int count() {
		Station temp = head;
		int cnt = 0;
		do {
			cnt++;
			temp = temp.nextLink;
		} while (temp != head);
		return cnt;
	}

	public void delete(String x) {
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

	public int minStation(String startStation, String destination) {
		Station start = findNode(startStation);
		Station startP = findNode(startStation);
		Station startQ = findNode(startStation);
		int cntP = 0, cntQ = 0;

		if (startStation.equals(destination))
			return 0;

		while (startP.nextLink != start) {
			if (startP.name.equals(destination))
				break;
			cntP++;
			startP = startP.nextLink;
		}

		while (startQ.preLink != start) {
			if (startQ.name.equals(destination))
				break;
			cntQ++;
			startQ = startQ.preLink;
		}

		if (cntP == count() - 1 && cntQ == count() - 1)
			return -1;

		return (cntP > cntQ) ? cntQ : cntP;
	}

	// 구현중
//	public String docking() {
//
//		return "";
//
//	}
}

public class Subway1 {

	public static void main(String[] args) {
		StationList stl = new StationList();

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
		// 구현
		stl.print();

		// 잠실역 제거
		stl.delete("잠실");
		stl.print();

		// 왕십리에서 잠실까지 최소 정거장
		// stl.addFirst("잠실");
		int minRes = stl.minStation("왕십리", "잠실");
		System.out.println(((minRes != -1) ? "왕십리에서 잠실까지 최소 " + minRes : "잠실역이 존재하지 않습니다."));

		// 신천쪽, 강변쪽으로 잠실 출발 / 어느 역에서 만나는지?
		// ???

		// 정거장의 숫자는 몇개인지?
		System.out.println("2호선 역의 갯수는 " + stl.count());

		// 방배역과 사당역 사이 별당역 추가
		stl.addMid("별당", "방배", "사당");
		stl.print();
	}

}