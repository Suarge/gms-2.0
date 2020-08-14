package gms.cuit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
//import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gms_Order implements Serializable {

	/** 订单编号 */
    private String order_Id ;
    /** 场馆编号 */
    private Gms_Venue order_Venue ;
    /** 工号/学号 */
    private Gms_User order_User ;
    /** 预约日期 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date order_Date ;
    /** 预约开始时间 */
    private Integer order_St ;
    /** 预约结束时间 */
    private Integer order_Ed ;
    /** 预约状态 */
    private Integer order_State ;//0代表进行中 (预约成功), 1代表已取消, 2代表已成功
    /** 订单生成时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date order_Mktime ;
    /** 订单价格 */
    private Double order_Price ;

	@Override
	public String toString() {
		return "Gms_Order{" +
				"order_Id='" + order_Id + '\'' +
				", order_Venue=" + order_Venue +
				", order_User=" + order_User +
				", order_Date=" + order_Date +
				", order_St=" + order_St +
				", order_Ed=" + order_Ed +
				", order_State=" + order_State +
				", order_Mktime=" + order_Mktime +
				", order_Price=" + order_Price +
				'}';
	}

	public Gms_Order() {
		super();
	}

	public String getOrder_Id() {
		return order_Id;
	}

	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}

	public Gms_Venue getOrder_Venue() {
		return order_Venue;
	}

	public void setOrder_Venue(Gms_Venue order_Venue) {
		this.order_Venue = order_Venue;
	}

	public Gms_User getOrder_User() {
		return order_User;
	}

	public void setOrder_User(Gms_User order_User) {
		this.order_User = order_User;
	}

	public Date getOrder_Date() {
		return order_Date;
	}

	public void setOrder_Date(Date order_Date) {
		this.order_Date = order_Date;
	}

	public Integer getOrder_St() {
		return order_St;
	}

	public void setOrder_St(Integer order_St) {
		this.order_St = order_St;
	}

	public Integer getOrder_Ed() {
		return order_Ed;
	}

	public void setOrder_Ed(Integer order_Ed) {
		this.order_Ed = order_Ed;
	}

	public Integer getOrder_State() {
		return order_State;
	}

	public void setOrder_State(Integer order_State) {
		this.order_State = order_State;
	}

	public Date getOrder_Mktime() {
		return order_Mktime;
	}

	public void setOrder_Mktime(Date order_Mktime) {
		this.order_Mktime = order_Mktime;
	}

	public Double getOrder_Price() {
		return order_Price;
	}

	public void setOrder_Price(Double order_Price) {
		this.order_Price = order_Price;
	}
}
