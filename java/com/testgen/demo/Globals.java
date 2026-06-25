package com.testgen.demo;

public class Globals {

    // user
    private static String userName;
    private static String userSurname;
    private static String userEmail;

    public String getUserName() { return userName; }
    public String getUserSurname() { return userSurname; }
    public String getUserEmail() { return userEmail; }

    public void setUserName (String name) { userName = name; }
    public void setUserSurname (String surname) { userSurname = surname; }
    public void setUserEmail (String email) { userEmail = email; }
}
