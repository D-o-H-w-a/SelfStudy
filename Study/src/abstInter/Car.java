package abstInter;

public class Car implements Vehicle {

    @Override
    public void move() {
        System.out.println("차량을 타고 이동한다");
    }

    @Override
    public void start() {
        System.out.println("차량의 시동을 건다");
    }
}
