package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/9/10.
 */
public class BannerModel implements Serializable {
    private String Title;
    private String Pic;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }
}
