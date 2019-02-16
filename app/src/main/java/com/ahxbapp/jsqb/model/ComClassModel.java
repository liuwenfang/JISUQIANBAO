package com.ahxbapp.jsqb.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xp on 16/8/27.
 */
public class ComClassModel extends DataSupport implements Serializable{
    private int ID;
    private String Name;
    private List<ComClassModel> fenl;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<ComClassModel> getFenl() {
        return fenl;
    }

    public void setFenl(List<ComClassModel> fenl) {
        this.fenl = fenl;
    }
}
