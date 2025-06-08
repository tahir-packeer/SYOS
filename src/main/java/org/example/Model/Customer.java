package org.example.Model;

public class Customer {
    private int id;
    private String name;
    private String contactNumber;

    public Customer(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcontactNumber() {
        return contactNumber;
    }

    public void setcontactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}