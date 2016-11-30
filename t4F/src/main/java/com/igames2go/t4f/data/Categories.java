
package com.igames2go.t4f.data;

public class Categories {

    private CategoriesDataObject position0 = new CategoriesDataObject();
    private CategoriesDataObject position1 = new CategoriesDataObject();
    private CategoriesDataObject position2 = new CategoriesDataObject();
    private CategoriesDataObject position3 = new CategoriesDataObject();
    private CategoriesDataObject position4 = new CategoriesDataObject();
    private CategoriesDataObject position5 = new CategoriesDataObject();
    private CategoriesDataObject position6 = new CategoriesDataObject();

    public CategoriesDataObject getCategoryPosition0() {
        return position0;
    }

    public CategoriesDataObject getCategoryPosition1() {
        return position1;
    }

    public CategoriesDataObject getCategoryPosition2() {
        return position2;
    }

    public CategoriesDataObject getCategoryPosition3() {
        return position3;
    }

    public CategoriesDataObject getCategoryPosition4() {
        return position4;
    }

    public CategoriesDataObject getCategoryPosition5() {
        return position5;
    }

    public CategoriesDataObject getCategoryPosition6() {
        return position6;
    }

    @Override
    public String toString() {

        return "Categories: " + "\nposition0: " + position0 + "\nposition1: "
                + position1 + "\nposition2: " + position2 + "\nposition3: "
                + position3 + "\nposition4: " + position4 + "\nposition5: "
                + position5 + "\nposition6: " + position6;

    }

}
