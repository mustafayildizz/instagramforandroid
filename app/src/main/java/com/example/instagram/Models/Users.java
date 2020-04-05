package com.example.instagram.Models;

public class Users {

    private String email;
    private String password;
    private String user_name;
    private String name_surname;
    private String phone_no;
    private String email_phone_no;
    private String user_id;

      public Users() {

    }

    public Users(String email, String password, String user_name,  String phone_no, String email_phone_no, String name_surname, String user_id) {
        this.email = email;
        this.password = password;
        this.user_name = user_name;
        this.name_surname = name_surname;
        this.user_id = user_id;
        this.phone_no = phone_no;
        this.email_phone_no = email_phone_no;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName_surname() {
        return name_surname;
    }

    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail_phone_no() {
        return email_phone_no;
    }

    public void setEmail_phone_no(String email_phone_no) {
        this.email_phone_no = email_phone_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
