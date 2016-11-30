
package com.igames2go.t4f.data;

public class CategoriesDataObject {

    private String catid;
    private String catnamelong;
    private String catnameshort;
    private String catimage;
    private String catimage_x;
    private String catimage_go;
    private String catimage_wide;
    private String catx;
    private String catsurprise;
    private String cat_point_value;

    public String getCatid() {
        return catid;
    }

    public String getCatnamelong() {
        return catnamelong;
    }

    public String getCatnameshort() {
        return catnameshort;
    }

    public String getCatimage() {
        return catimage;
    }

    public String getCatimage_x() {
        return catimage_x;
    }

    public String getCatimage_go() {
        return catimage_go;
    }

    public String getCatimage_wide() {
        return catimage_wide;
    }

    public String getCatx() {
        return catx;
    }

    public String getCatsurprise() {
        return catsurprise;
    }

    public String getCat_point_value() {
        return cat_point_value;
    }

    @Override
    public String toString() {
        return "CategoriesDataObject: \ncatid: " + catid + "\ncatnamelong: "
                + catnamelong + "\ncatnameshort: " + catnameshort
                + "\ncatimage: " + catimage + "\ncatimage_x: " + catimage_x
                + "\ncatimage_go: " + catimage_go + "\ncatimage_wide: "
                + catimage_wide + "\ncatx: " + catx + "\ncatsurprise: "
                + catsurprise + "\ncat_point_value: " + cat_point_value;
    }

}
