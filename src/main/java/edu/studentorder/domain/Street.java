package edu.studentorder.domain;

public class Street {
    private long street_code;
    private String street_name;

    public Street() {
    }

    public Street(long street_code, String street_name) {
        this.street_code = street_code;
        this.street_name = street_name;
    }

    public long getStreet_code() {
        return street_code;
    }

    public void setStreet_code(long street_code) {
        this.street_code = street_code;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }
}
