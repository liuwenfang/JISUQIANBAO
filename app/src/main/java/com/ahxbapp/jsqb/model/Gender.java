package com.ahxbapp.jsqb.model;

/**
 * Created by gravel on 16/8/14.
 */

public enum Gender {
    Male(1),Female(0);
    private int index;
    public  int getIndex(){
        return index;
    }
    private Gender(int i){
        index=i;
    }
}
