package gms.cuit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

public class Gms_Notice implements Serializable{

	/** 通知编号 */
    private String notice_Id ;
    /** 通知日期 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date notice_Time ;
    /** 通知人 ,暂时不用其他实体,方便拓展 */
    private String notice_Man ;
    /** 通知内容 */
    private String notice_Content ;
    /** 通知标题 */
    private String notice_Title ;
    /** 通知状态;为0代表通知未被删除,1代表通知被删除了 */
    private Integer notice_State ;

	public Gms_Notice() {
	}

	@Override
	public String toString() {
		return "Gms_Notice{" +
				"notice_Id='" + notice_Id + '\'' +
				", notice_Time=" + notice_Time +
				", notice_Man='" + notice_Man + '\'' +
				", notice_Content='" + notice_Content + '\'' +
				", notice_Title='" + notice_Title + '\'' +
				", notice_State=" + notice_State +
				'}';
	}

	public String getNotice_Id() {
		return notice_Id;
	}

	public void setNotice_Id(String notice_Id) {
		this.notice_Id = notice_Id;
	}

	public Date getNotice_Time() {
		return notice_Time;
	}

	public void setNotice_Time(Date notice_Time) {
		this.notice_Time = notice_Time;
	}

	public String getNotice_Man() {
		return notice_Man;
	}

	public void setNotice_Man(String notice_Man) {
		this.notice_Man = notice_Man;
	}

	public String getNotice_Content() {
		return notice_Content;
	}

	public void setNotice_Content(String notice_Content) {
		this.notice_Content = notice_Content;
	}

	public String getNotice_Title() {
		return notice_Title;
	}

	public void setNotice_Title(String notice_Title) {
		this.notice_Title = notice_Title;
	}

	public Integer getNotice_State() {
		return notice_State;
	}

	public void setNotice_State(Integer notice_State) {
		this.notice_State = notice_State;
	}
}
