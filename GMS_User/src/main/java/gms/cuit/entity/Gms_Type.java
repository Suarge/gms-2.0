package gms.cuit.entity;

import java.io.Serializable;

public class Gms_Type implements Serializable{

    /** 场馆类型名 */
    private String type_Name ;
    /** 联系人 */
    private String type_Linkman ;
    /** 场馆地址 */
    private String type_Address ;
    /** 场馆简介 */
    private String type_Introduction ;
    
	public Gms_Type() {
		super();
	}

	public Gms_Type(String type_Name, String type_Linkman, String type_Address,
			String type_Introduction) {
		super();
		this.type_Name = type_Name;
		this.type_Linkman = type_Linkman;
		this.type_Address = type_Address;
		this.type_Introduction = type_Introduction;
	}


	public String getType_Name() {
		return type_Name;
	}

	public void setType_Name(String type_Name) {
		this.type_Name = type_Name;
	}
	
	public void setType_name(String type_Name) {
		this.type_Name = type_Name;
	}

	public String getType_Linkman() {
		return type_Linkman;
	}

	public void setType_Linkman(String type_Linkman) {
		this.type_Linkman = type_Linkman;
	}
	
	public void setType_linkman(String type_Linkman) {
		this.type_Linkman = type_Linkman;
	}

	public String getType_Address() {
		return type_Address;
	}

	public void setType_Address(String type_Address) {
		this.type_Address = type_Address;
	}
	
	public void setType_address(String type_Address) {
		this.type_Address = type_Address;
	}

	public String getType_Introduction() {
		return type_Introduction;
	}

	public void setType_Introduction(String type_Introduction) {
		this.type_Introduction = type_Introduction;
	}
	
	public void setType_introduction(String type_Introduction) {
		this.type_Introduction = type_Introduction;
	}

	@Override
	public String toString() {
		return "Gms_Type [type_Name=" + type_Name + ", type_Linkman=" + type_Linkman
				+ ", type_Address=" + type_Address + ", type_Introduction=" + type_Introduction + "]";
	}



}