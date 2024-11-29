package singletonStudy;

import java.util.Objects;

public class Zoo {

    private String name;

    public void prn() {
        System.out.println("동물원을 개장하였으며 동물원 이름은 " + name + " 입니다.");
    }

    public String getName() {
        if (Objects.isNull(name)) {
            System.err.println("선언한 이름이 없습니다.");
        }
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
