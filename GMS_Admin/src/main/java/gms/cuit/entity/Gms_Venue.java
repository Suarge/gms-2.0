package gms.cuit.entity;

import java.io.Serializable;

public class Gms_Venue implements Serializable{

	/** 场馆编号 */
    private String venue_Id ;
    /** 场馆类别 */
    private String venue_Type ;
    /** 场馆名字 */
    private String venue_Name ;
    /** 场馆价格 */
    private Double venue_Price ;
    /** 场馆接待能力 */
    private Integer venue_Capacity ;
    /** 开始时间 */
    private Integer venue_Open ;
    /** 关闭时间 */
    private Integer venue_Close ;
    /** 场馆是否被删除;为0代表场馆未被删除,1代表场馆被删除了 */
    private Integer venue_IsDel ;
    
	public Gms_Venue() {
		super();
	}

	@Override
	public String toString() {
		return "Gms_Venue{" +
				"venue_Id='" + venue_Id + '\'' +
				", venue_Type='" + venue_Type + '\'' +
				", venue_Name='" + venue_Name + '\'' +
				", venue_Price=" + venue_Price +
				", venue_Capacity=" + venue_Capacity +
				", venue_Open=" + venue_Open +
				", venue_Close=" + venue_Close +
				", venue_IsDel=" + venue_IsDel +
				'}';
	}

	public String getVenue_Id() {
		return venue_Id;
	}

	public void setVenue_Id(String venue_Id) {
		this.venue_Id = venue_Id;
	}

	public String getVenue_Type() {
		return venue_Type;
	}

	public void setVenue_Type(String venue_Type) {
		this.venue_Type = venue_Type;
	}

	public String getVenue_Name() {
		return venue_Name;
	}

	public void setVenue_Name(String venue_Name) {
		this.venue_Name = venue_Name;
	}

	public Double getVenue_Price() {
		return venue_Price;
	}

	public void setVenue_Price(Double venue_Price) {
		this.venue_Price = venue_Price;
	}

	public Integer getVenue_Capacity() {
		return venue_Capacity;
	}

	public void setVenue_Capacity(Integer venue_Capacity) {
		this.venue_Capacity = venue_Capacity;
	}

	public Integer getVenue_Open() {
		return venue_Open;
	}

	public void setVenue_Open(Integer venue_Open) {
		this.venue_Open = venue_Open;
	}

	public Integer getVenue_Close() {
		return venue_Close;
	}

	public void setVenue_Close(Integer venue_Close) {
		this.venue_Close = venue_Close;
	}

	public Integer getVenue_IsDel() {
		return venue_IsDel;
	}

	public void setVenue_IsDel(Integer venue_IsDel) {
		this.venue_IsDel = venue_IsDel;
	}
}
