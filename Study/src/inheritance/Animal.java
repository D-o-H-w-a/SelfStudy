package inheritance;

public class Animal {

    // 동물 번호
    protected int id;
    // 동물 이름
    protected String name;
    // 동물 나이
    protected int age;
    // 동물 종류
    protected String type;
    // 동물 출생일
    protected String birth;

    // 동물 번호 값 받아오기
    public void getId(int id) {
        this.id = id;
    }

    // 동물 번호 값 넘겨주기
    public int setId() {
        return id;
    }
    // 동물 이름 값 받아오기
    public void getName(String name) {
        this.name = name;
    }

}
