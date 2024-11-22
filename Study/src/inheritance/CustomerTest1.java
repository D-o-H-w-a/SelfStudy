package inheritance;

public class CustomerTest1 {

    public static void main(String[] args) {

        Customer customerLee = new Customer();
        customerLee.setCustomerID(10010);
        customerLee.setCustomerName("이순신");
        customerLee.bonusPoint = 1000;
        System.out.println(customerLee.showCustomerInfo());

        VIPCustomer customerKim = new VIPCustomer();
        customerKim.setCustomerID(10020);
        customerKim.setCustomerName("김유신");
        customerKim.bonusPoint = 10000;
        System.out.println(customerKim.showCustomerInfo());

        Animal lion = new Animal(1, "사자", 5, "콩고", "2019-01-13");
        System.out.println("현재 동물의 번호는 " + lion.id + "번 이며 동물의 이름은 " + lion.name + "이고"
        + " 나이는 " + lion.age + "살이며 동물의 종은 " + lion.type + "입니다. 해당 동물의 출생일은 " + lion.birth + " 입니다.");

        Animal tiger = new Animal(2, "호랑이", 7, "벵골", "2017-04-22");
        System.out.println("현재 동물의 번호는 " + tiger.id + "번 이며 동물의 이름은 " + tiger.name + "이고"
                + " 나이는 " + tiger.age + "살이며 동물의 종은 " + tiger.type + "입니다. 해당 동물의 출생일은 " + tiger.birth + " 입니다.");
    }
}
