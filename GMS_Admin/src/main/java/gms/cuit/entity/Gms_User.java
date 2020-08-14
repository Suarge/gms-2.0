package gms.cuit.entity;

import java.io.Serializable;

public class Gms_User implements Serializable{

	/** 工号/学号 */
    private String user_Id ;
    /** 密码 */
    private String user_Password ;
    /** 性别 */
    private String user_Sex ;
    /** 年龄 */
    private Integer user_Age ;
    /** 邮箱 */
    private String user_Email ;
    
	public Gms_User() {
		super();
	}

	public Gms_User(String user_Id, String user_Password, String user_Sex, Integer user_Age, String user_Email) {
		super();
		this.user_Id = user_Id;
		this.user_Password = user_Password;
		this.user_Sex = user_Sex;
		this.user_Age = user_Age;
		this.user_Email = user_Email;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	
	public void setUser_id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getUser_Password() {
		return user_Password;
	}

	public void setUser_Password(String user_Password) {
		this.user_Password = user_Password;
	}
	
	public void setUser_password(String user_Password) {
		this.user_Password = user_Password;
	}

	public String getUser_Sex() {
		return user_Sex;
	}

	public void setUser_Sex(String user_Sex) {
		this.user_Sex = user_Sex;
	}
	
	public void setUser_sex(String user_Sex) {
		this.user_Sex = user_Sex;
	}

	public Integer getUser_Age() {
		return user_Age;
	}

	public void setUser_Age(Integer user_Age) {
		this.user_Age = user_Age;
	}
	
	public void setUser_age(Integer user_Age) {
		this.user_Age = user_Age;
	}

	public String getUser_Email() {
		return user_Email;
	}

	public void setUser_Email(String user_Email) {
		this.user_Email = user_Email;
	}
	
	public void setUser_email(String user_Email) {
		this.user_Email = user_Email;
	}

	@Override
	public String toString() {
		return "Gms_User [user_Id=" + user_Id + ", user_Password=" + user_Password + ", user_Sex=" + user_Sex
				+ ", user_Age=" + user_Age + ", user_Email=" + user_Email + "]";
	}

	
}