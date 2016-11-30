
package com.igames2go.t4f.data;

public class FB_FriendsDataObject {

    FB_FriendsPicture picture = new FB_FriendsPicture();
    String id;
    String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FB_FriendsPicture getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n name: " + name + "\n picture: " + picture;
    }
}
