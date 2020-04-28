package com.example.dbsave;

public class Contact {
    private String _name;
    private int _id;
    private String _phone_number;

    Contact() {

    }

    Contact(String fname, String phone_number) {
        this._name = fname;
        this._phone_number = phone_number;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    String getName() {
        return this._name;
    }

    void setName(String fname) {
        this._name = fname;
    }


    String getPhoneNumber() {
        return this._phone_number;
    }

    void setPhoneNumber(String phone_number) {
        this._phone_number = phone_number;
    }

}