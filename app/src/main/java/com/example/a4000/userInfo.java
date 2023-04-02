package com.example.a4000;

public class userInfo
{
    private String userName;

    private String dob;

    private String email;

    public userInfo()
    {

    }

    public userInfo(String userName, String dob, String email)
    {
        this.userName = userName;
        this.dob = dob;
        this.email = email;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDob()
    {
        return dob;
    }

    public void setDob(String dob)
    {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
