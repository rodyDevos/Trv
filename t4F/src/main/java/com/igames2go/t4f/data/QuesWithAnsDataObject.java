
package com.igames2go.t4f.data;

import java.io.Serializable;

public class QuesWithAnsDataObject implements Serializable {

    private String queid;
    private String text;
    private String type;

    public String getQueid() {
        return queid;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "QuesWithAnsDataObject: \nqueid: " + queid + "\ntext: " + text
                + "\ntype: " + type;
    }

}
