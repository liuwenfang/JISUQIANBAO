package com.ahxbapp.jsqb.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26 0026.
 * 省份ID
 */
public class ProvinceModel {

    private int ID;
    private String Name;
    private List<CityModel> city;

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public List<CityModel> getCity() {
        return city;
    }
    public void setCity(List<CityModel> city) {
        this.city = city;
    }

    public static class CityModel{
        private int ID;
        private String Name;
        private List<Area> area;

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
            this.Name = name;
        }

        public List<Area> getArea() {
            return area;
        }
        public void setArea(List<Area> area) {
            this.area = area;
        }

        public static class Area{
            private int ID;
            private String Name;

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


}


