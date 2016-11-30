
package com.igames2go.t4f.data;

import java.util.ArrayList;
import java.util.List;

public class FB_Friends {

    private List<FB_FriendsDataObject> data = new ArrayList<FB_FriendsDataObject>();

    public List<FB_FriendsDataObject> getFB_Friends() {
        return data;
    }

    @Override
    public String toString() {
        return "" + data;
    }

}
