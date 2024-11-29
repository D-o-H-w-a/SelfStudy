package singletonStudy;

public class SingletonManager {

    private static SingletonManager singManager;

    private CarList carList;

    public static SingletonManager instance() {
        if (singManager == null) {
            singManager = new SingletonManager();
        }

        return singManager;
    }

    public CarList getCarList() {
        if (carList == null) {
            carList = new CarList();
        }
        return carList;
    }
    public void setCarList(CarList carList) {
        this.carList = carList;
    }
}
