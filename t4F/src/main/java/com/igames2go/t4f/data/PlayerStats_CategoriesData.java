
package com.igames2go.t4f.data;

public class PlayerStats_CategoriesData {

    private String order;
    private String imagewide;
    private String catright;
    private int catasked;
    private String percentright;
    private String category_award_image;
    private String catpoint_tot;

    public String getOrder() {
        return order;
    }

    public String getImagewide() {
        return imagewide;
    }

    public String getCatright() {
        return catright;
    }

    public int getCatasked() {
        return catasked;
    }

    public String getPercentright() {
        return percentright;
    }

    public String getCategory_award_image() {
        return category_award_image;
    }

    public String getCatpoint_tot() {
        return catpoint_tot;
    }

    @Override
    public String toString() {
        return "PlayerStats_CategoriesData: \norder: " + order
                + "\nimagewide: " + imagewide + "\ncatright: " + catright
                + "\ncatasked: " + catasked + "\npercentright: " + percentright
                + "\ncategory_award_image: " + category_award_image
                + "\ncatpoint_tot: " + catpoint_tot;
    }

}
