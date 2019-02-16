package com.ahxbapp.jsqb.model;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class BaseModel<T> {
    private int result;
    private String message;
    private T data;

    @Override
    public String toString() {
        return "BaseModel{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
