package dto;

import java.io.Serializable;

public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name, lastName;
    private int birthYear, phoneNumber;

    private Address registeredAddress;
    private Address residentialAddress;

    public Patient(String name, String lastName, int birthYear, int phoneNumber, Address registeredAddress, Address residentialAddress) {
        this.name = name;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.phoneNumber = phoneNumber;
        this.registeredAddress = registeredAddress;
        this.residentialAddress = residentialAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(Address registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public Address getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(Address residentialAddress) {
        this.residentialAddress = residentialAddress;
    }
}
