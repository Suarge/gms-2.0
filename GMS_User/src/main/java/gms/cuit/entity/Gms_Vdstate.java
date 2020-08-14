package gms.cuit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class Gms_Vdstate implements Serializable{

	/** 场馆编号 */
	private Gms_Venue gms_venue;
    /** 日期 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+08")
    private Date vdstate_Date ;
    /** 分时状态;第i位代表第i~i+1小时的场馆状态0代表不可用1代表可用2代表已经被预约 */
    private String vdstate_St ;
    
	public Gms_Vdstate() {
		super();
	}

	@Override
	public String toString() {
		return "Gms_Vdstate{" +
				"gms_venue=" + gms_venue +
				", vdstate_Date=" + vdstate_Date +
				", vdstate_St='" + vdstate_St + '\'' +
				'}';
	}

	public Gms_Venue getGms_venue() {
		return gms_venue;
	}

	public void setGms_venue(Gms_Venue gms_venue) {
		this.gms_venue = gms_venue;
	}
	public Date getVdstate_Date() {
		return vdstate_Date;
	}

	public void setVdstate_Date(Date vdstate_Date) {
		this.vdstate_Date = vdstate_Date;
	}

	public String getVdstate_St() {
		return vdstate_St;
	}

	public void setVdstate_St(String vdstate_St) {
		this.vdstate_St = vdstate_St;
	}
}
