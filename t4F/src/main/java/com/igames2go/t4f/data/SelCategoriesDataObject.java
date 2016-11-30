
package com.igames2go.t4f.data;

import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

public class SelCategoriesDataObject {

    public static final int EXPAND_FLAG_EMPTY = 0;
    public static final int EXPAND_FLAG_NO = 1;
    public static final int EXPAND_FLAG_PLUS = 2;
    public static final int EXPAND_FLAG_BUY = 3;

    public static final int FAVOR_NO = 0;
    public static final int FAVOR_YES = 1;
    public static final int FAVOR_EMPTY = 2;

    public static final int SECTION_FAVOR = 0;
    public static final int SECTION_CATEGORY = 1;
    public static final int SECTION_GROUP = 2;

    public static final int ACTION_EMPTY = 0;
    public static final int ACTION_SELECT = 1;
    public static final int ACTION_UNSELECT = 2;
    public static final int ACTION_BUY = 3;

    private String catid;
    private String catname;
    private int section;

    private int expandflag;
    private String groupid;
    private String groupname;
    private int action;
    private int favor;
    private String favorprice;
    private String disclosureurl;
    private String productid;
    private String imagewide;
    private String imagesquare;

    private boolean isExpanded;

    private boolean isHidden;

    public SelCategoriesDataObject() {

        this.disclosureurl = "";
    }
    public SelCategoriesDataObject(SelCategoriesDataObject obj){
        this.catid = obj.getCatid();
        this.catname = obj.getCatname();
        this.section = obj.getSecion();
        this.expandflag = obj.getExpandflag();
        this.groupid = obj.getGroupid();
        this.groupname = obj.getGroupname();
        this.action = obj.getAction();
        this.favor = obj.getFavor();
        this.favorprice = obj.getFavorprice();
        this.disclosureurl = obj.getDisclosureurl();
        this.productid = obj.getProductid();
        this.imagesquare = obj.getImagesquare();
        this.imagewide = obj.getImagewide();
        this.isExpanded = obj.isExpanded();
        this.isHidden = obj.isHidden();
    }
    public String getCatid() {
        return catid;
    }

    public String getCatname() {
        return catname;
    }

    public int getSecion() {
        return section;
    }

    public int getExpandflag() {
        return expandflag;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public int getAction() { return action;    }

    public int getFavor() { return favor;    }

    public String getFavorprice() {
        return favorprice;
    }

    public String getDisclosureurl() {
        return disclosureurl;
    }

    public String getProductid() {
        return productid;
    }

    public String getImagewide() {
        return imagewide;
    }

    public String getImagesquare() {
        return imagesquare;
    }

    public boolean isHidden() { return isHidden; }

    public boolean isExpanded() { return  isExpanded; }

    public void setExpanded( boolean expandFlag) { isExpanded = expandFlag; }

    public void setSection( int s) { section = s; }

    public void setFavor (int flag) { favor = flag; }

    public void setAction (int flag) { action = flag; }

    public void setHidden (boolean flag) { isHidden = flag; }

    public void parseJSONObject(JSONObject obj){

        try {

            if(obj.getString("section").equalsIgnoreCase("FAVORITES")){
                section = SECTION_FAVOR;
            }else if(obj.getString("section").equalsIgnoreCase("CATEGORIES")){
                section = SECTION_CATEGORY;
            }else{
                section = SECTION_GROUP;
            }

            if (obj.getString("expandflag").length() == 0) {
                expandflag = EXPAND_FLAG_EMPTY;
            }else if (obj.getString("expandflag").equalsIgnoreCase("no")) {
                expandflag = EXPAND_FLAG_NO;
            }else if (obj.getString("expandflag").equalsIgnoreCase("buy")) {
                expandflag = EXPAND_FLAG_BUY;
            }else {
                expandflag = EXPAND_FLAG_PLUS;
            }

            favorprice = obj.getString("favorprice");

            if (favorprice.equalsIgnoreCase("favor")) {
                favor = FAVOR_NO;
            }else if (favorprice.equalsIgnoreCase("unfavor")) {
                favor = FAVOR_YES;
            }else{
                favor = FAVOR_EMPTY;
            }

            if (obj.getString("button").length() == 0) {
                action = ACTION_EMPTY;
            }else if (obj.getString("button").equalsIgnoreCase("select")) {
                action = ACTION_UNSELECT;
            }else if (obj.getString("button").equalsIgnoreCase("unselect")) {
                action = ACTION_SELECT;
            }else{
                action = ACTION_BUY;
            }

            groupid = obj.getString("groupid");
            groupname = obj.getString("groupname");
            catid = obj.getString("catid");
            catname = obj.getString("catname");

            disclosureurl = obj.getString("disclosureurl");
            productid = obj.getString("productid");
            imagewide = obj.getString("imagewide");
            imagesquare = obj.getString("imagesquare");

            isExpanded = false;

            if (obj.getString("hide").equalsIgnoreCase("1"))
                isHidden = true;
            else
                isHidden = false;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "SelCategoriesDataObject: \ncatid: " + catid + "\ncatname: "
                + catname + "\nsection: " + section
                + "\nexpandflag: " + expandflag + "\ngroupid: " + groupid
                + "\ngroupname: " + groupname + "\naction: "
                + action + "\nfavorprice: " + favorprice + "\ndisclosureurl: "
                + disclosureurl + "\nproductid: " + productid + "\nimagewide: "
                + imagewide + "\nimagesquare: " + imagesquare + "isExpanded: " + isExpanded;
    }

}
