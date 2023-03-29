package com.example.a4000;

public class userInfo
{
    private String user_name;

    private String dob;

    private String email;

    public userInfo()
    {

    }

    public userInfo(String user_name, String dob, String email)
    {
        this.user_name = user_name;
        this.dob = dob;
        this.email = email;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
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
