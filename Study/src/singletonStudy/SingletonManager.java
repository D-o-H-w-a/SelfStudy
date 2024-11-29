package singletonStudy;

public class SingletonManager {

    private static SingletonManager singManager;

    private CarList zoo;
    public static SingletonManager instance() {
        if (singManager == null) {
            singManager = new SingletonManager();
        }

        return singManager;
    }
}
