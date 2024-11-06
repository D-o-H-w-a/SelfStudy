package inheritance;

public class VIPCustomer extends Customer {

    private int agentID; // VIP 고객 상담원 아이디
    double saleRatio; // 할인율

    public VIPCustomer() {

        customerGrade = "VIP";
        bonusRatio = 0.05;
        saleRatio = 0.1;

        // 하위  클래스 생성 할 시 콘솔 출력문
        System.out.println("VIPCustomer() 생성자 호출 ");
    }

    public int getAgentID() {
        return agentID;
    }
}


// 자식 생성자 선언 전 클래스
/*
public class VIPCustomer {

	// 멤버변수
	private int customerID; // 고객 아이디
	private String customerName = "이율"; // 고객 이름
	private String customerGrade; // 고객 등급
	int bonusPoint; // 보너스 포인트
	double bonusRatio; // 적립 비율

	private int agentID; // VIP 고객 담당 상담원 아이디
	double saleRatio; // 할인율


	// 디폴트 생성자
	public VIPCustomer() {
		customerGrade = "VIP"; //고객 등급 VIP
		bonusRatio = 0.05; // 보너스 적립 5%
		saleRatio = 0.1; // 할인율 10%
	}

	// 할인율 적용
	public int calcPrice(int price) {
		bonusPoint += price * bonusRatio;
		return price - (int)(price * saleRatio);
	}

	// VIP 고객에게만 필요한 메서드
	public int getAgentID() {
		return agentID;
	}

	public String showCustomerInfo() {
		return customerName + " 님의 등급은 " + customerGrade + " 이며, 보너스 포인트는 " + bonusPoint + " 입니다.";
	}
}
*/