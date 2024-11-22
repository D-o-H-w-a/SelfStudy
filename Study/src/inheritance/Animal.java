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

    // 동물 번호 값 넘겨주기
    public int getId() {
        return id;
    }
    // 동물 번호 값 받아오기
    public void setId(int id) {
        this.id = id;
    }


    // 동물 이름 값 넘겨주기
    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }
    // 동물 이름 값 받아오기
    public void setName(String name) {
        this.name = name;
    }

    // 동물 나이 넘겨주기
    public int getAge() {
        return age;
    }
    // 동물 나이 받아오기
    public void setAge(int age) {
        this.age = age;
    }

    // 동물 종류 넘겨주기
    public String getType() {
        if (type == null) {
            return "";
        }
        return type;
    }
    // 동물 종류 받아오기
    public void setType(String type) {
        this.type = type;
    }
}