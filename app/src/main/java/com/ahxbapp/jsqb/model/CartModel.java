package com.ahxbapp.jsqb.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by xp on 16/9/2.
 */
public class CartModel implements Serializable, Parcelable {
    private String thumbnail,Title,Size,ComSize;
    private float Price;
    private int Num,ID,ComID;

    public CartModel(){}
    protected CartModel(Parcel in) {
        thumbnail = in.readString();
        Title = in.readString();
        Size = in.readString();
        ComSize = in.readString();
        Price = in.readFloat();
        Num = in.readInt();
        ID = in.readInt();
        ComID = in.readInt();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getComSize() {
        return ComSize;
    }

    public void setComSize(String comSize) {
        ComSize = comSize;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getComID() {
        return ComID;
    }

    public void setComID(int comID) {
        ComID = comID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail);
        dest.writeString(Title);
        dest.writeString(Size);
        dest.writeString(ComSize);
        dest.writeFloat(Price);
        dest.writeInt(Num);
        dest.writeInt(ID);
        dest.writeInt(ComID);
    }
}
