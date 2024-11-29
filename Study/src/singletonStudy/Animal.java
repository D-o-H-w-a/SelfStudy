package singletonStudy;

public class Animal {

    public static void main(String[] args) {
        Zoo zoo =  Animals.instance().getZoo();

        zoo.setName("기장");

        zoo.prn();
    }
}
