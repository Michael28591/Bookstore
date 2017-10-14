package com.a1694670.michael.bookstore;

/**
 * Created by admin on 2017-10-13.
 */

public class AuthorFeed {
    public String message;
    public String picture;
    public String authorid;

    public AuthorFeed(String msg, String pic,String id)
    {
        message = msg;
        picture = pic;
        authorid = id;

        if (message == null || picture ==null || authorid == null || message == "" || picture =="" || authorid == "" || message.isEmpty() || picture.isEmpty() || authorid.isEmpty()) {
            message="khali";
            authorid="khali";
            picture="https://images-na.ssl-images-amazon.com/images/I/51Rmbpdgh3L._SY200_.gif";
        }
    }
}
