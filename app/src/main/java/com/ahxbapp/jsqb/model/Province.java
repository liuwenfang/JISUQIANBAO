package com.ahxbapp.jsqb.model;

import java.util.List;

/**
 * Created by Admin on 2016/10/20.
 * Page
 */
public class Province {
    private int ID;
    private String Name;
    private List<AreaCity> AreaCity;

    @Override
    public String toString() {
        return "Province{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", AreaCity=" + AreaCity +
                '}';
    }

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

    public List<Province.AreaCity> getAreaCity() {
        return AreaCity;
    }

    public void setAreaCity(List<Province.AreaCity> areaCity) {
        AreaCity = areaCity;
    }

    public static class AreaCity {
        private int ID;
        private String Name;

        @Override
        public String toString() {
            return "AreaCity{" +
                    "ID=" + ID +
                    ", Name='" + Name + '\'' +
                    '}';
        }

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
    }
}
