package com.igames2go.t4f.data;

public class LoginUser {
	private String fname;
	private String lname;
	private String uname;
	private String uid;
	private String email;
	private String created_at;
	private String imageName;
	
	public String getFirstName() {
		return fname;
	}
	public void setFirstName(String fname) {
		this.fname = fname;
	}
	public String getLastName() {
		return lname;
	}
	public void setLastName(String lname) {
		this.lname = lname;
	}
	public String getUserName() {
		return uname;
	}
	public void setUserName(String uname) {
		this.uname = uname;
	}
	public String getUserId() {
		return uid;
	}
	public void setUserId(String uid) {
		this.uid = uid;
	}
	public String getCreatedDate() {
		return created_at;
	}
	public void setCreatedDate(String created_at) {
		this.created_at = created_at;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserImageName() {
		return imageName;
	}
	public void setUserImageName(String imageName) {
		this.imageName = imageName;
	}
}
