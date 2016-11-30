
package com.igames2go.t4f.data;

public class GameStatistics_CategoriesData {

    private String position;
    private String imagewide;
    private String unavailable;
    private String catright;
    private String catasked;
    private String cat_point_value;
    private String cat_earned_points;

    public String getPosition() {
        return position;
    }

    public String getImagewide() {
        return imagewide;
    }

    public String getUnavailable() {
        return unavailable;
    }

    public String getCatright() {
        return catright;
    }

    public String getCatasked() {
        return catasked;
    }

    public String getCat_point_value() {
        return cat_point_value;
    }

    public String getCat_earned_points() {
        return cat_earned_points;
    }

    @Override
    public String toString() {
        return "GameStatistics_CategoriesData: \nposition: " + position
                + "\nimagewide: " + imagewide + "\nunavailable: " + unavailable
                + "\ncatright: " + catright + "\ncatasked: " + catasked
                + "\ncat_point_value: " + cat_point_value
                + "\ncat_earned_points: " + cat_earned_points;
    }

}
