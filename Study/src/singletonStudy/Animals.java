package singletonStudy;

public class Animals {

    private static Animals animals;

    private Zoo zoo;
    public static Animals instance() {
        if (animals == null) {
            animals = new Animals();
        }

        return animals;
    }

    public Zoo getZoo() {
        if (zoo == null) {
            zoo = new Zoo();
        }
        return zoo;
    }

    public void setZoo(Zoo zoo) {
        this.zoo = zoo;
    }
}
