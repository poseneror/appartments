package com.poz.appartments;

/**
 * Created by Or on 01/08/2017.
 */

public class Seller {
    private int id;
    private String name;
    private int contact;
    private String email;

    public Seller(int id, String name, int contact, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
