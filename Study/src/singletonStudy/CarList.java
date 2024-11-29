package singletonStudy;

import java.awt.*;
import java.util.Objects;

public class CarList {

    private List company = new List();

    public List getCompany() {

        if (Objects.isNull(company)) {
            company = new List();
            return company;
        }

        return company;
    }

    public void setCompany(List company) {
        this.company = company;
    }
}
