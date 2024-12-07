package abstInter;

public class Play {

    public static void main(String[] args) {
        Car car = new Car();

        car.engine();
        car.move("전방");
        car.speed(20, 50);

        System.out.println();

        Bike bike = new Bike();

        bike.engine();
        bike.move("좌측");
        bike.speed(15, 80);
        bike.willy();


    }
}
