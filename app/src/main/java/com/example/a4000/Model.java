package com.example.a4000;

public class Model
{
    private String imageUrl;

    public Model()
    {

    }

    public Model(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
