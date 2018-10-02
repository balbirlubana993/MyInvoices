package com.example.balbir.myinvoices;

import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by Parsania Hardik on 26-Apr-17.
 */
public class UserModel implements Serializable{
    private String title,shop,invoice,loc,comment,date;
    private Blob b;
    private int id;
byte[] image;
  //  private Blob image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getshop() {
        return shop;
    }

    public void setshop(String shop) {
        this.shop= shop;
    }
    public String getinvoice() {
        return invoice;
    }

    public void setinvoice(String invoice) {
        this.invoice= invoice;
    }
    public String getloc() {
        return loc;
    }

    public void setloc(String loc) {
        this.loc= loc;
    }
    public String getcomment() {
        return comment;
    }

    public void setcomment(String comment) {
        this.comment= comment;
    }
    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date= date;
    }
    public byte[] getImage(){
        return this.image;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
}
