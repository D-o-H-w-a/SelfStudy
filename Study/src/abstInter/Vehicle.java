package abstInter;

abstract public class Vehicle {

    abstract public void engine();

    public void move(String way) {
        System.out.println(way + "으로 달립니다.");
    }
    public void speed(int min, int max) {
        System.out.println("이동수단의 최저 속도는 " + min + "km/h 입니다.");
        System.out.println("이동수단의 최고 속도는 " + max + "km/h 입니다.");
    }
}

