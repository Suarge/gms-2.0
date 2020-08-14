package gms.cuit.entity;

public class Gms_Bck_analytics {

	//查询的名字
	private String analytics_Name;
	//收入情况
	private Double analytics_Profit;
	//租用人次
	private Integer analytics_Rent;
	//使用率
	private String analytics_Usage;
	//男女欢迎度
	private String analytics_Sexpercentage;
	//年龄分布
	private String analytics_Agepercentage;
	
	public Gms_Bck_analytics() {
		super();
	}

	public Gms_Bck_analytics(String analytics_Name, Double analytics_Profit, Integer analytics_Rent,
                             String analytics_Usage, String analytics_Sexpercentage, String analytics_Agepercentage) {
		super();
		this.analytics_Name = analytics_Name;
		this.analytics_Profit = analytics_Profit;
		this.analytics_Rent = analytics_Rent;
		this.analytics_Usage = analytics_Usage;
		this.analytics_Sexpercentage = analytics_Sexpercentage;
		this.analytics_Agepercentage = analytics_Agepercentage;
	}

	public String getAnalytics_Name() {
		return analytics_Name;
	}

	public void setAnalytics_Name(String analytics_Name) {
		this.analytics_Name = analytics_Name;
	}

	public Double getAnalytics_Profit() {
		return analytics_Profit;
	}

	public void setAnalytics_Profit(Double analytics_Profit) {
		this.analytics_Profit = analytics_Profit;
	}

	public Integer getAnalytics_Rent() {
		return analytics_Rent;
	}

	public void setAnalytics_Rent(Integer analytics_Rent) {
		this.analytics_Rent = analytics_Rent;
	}

	public String getAnalytics_Usage() {
		return analytics_Usage;
	}

	public void setAnalytics_Usage(String analytics_Usage) {
		this.analytics_Usage = analytics_Usage;
	}

	public String getAnalytics_Sexpercentage() {
		return analytics_Sexpercentage;
	}

	public void setAnalytics_Sexpercentage(String analytics_Sexpercentage) {
		this.analytics_Sexpercentage = analytics_Sexpercentage;
	}

	public String getAnalytics_Agepercentage() {
		return analytics_Agepercentage;
	}

	public void setAnalytics_Agepercentage(String analytics_Agepercentage) {
		this.analytics_Agepercentage = analytics_Agepercentage;
	}

	@Override
	public String toString() {
		return "Gms_Bck_analytics [analytics_Name=" + analytics_Name + ", analytics_Profit=" + analytics_Profit
				+ ", analytics_Rent=" + analytics_Rent + ", analytics_Usage=" + analytics_Usage
				+ ", analytics_Sexpercentage=" + analytics_Sexpercentage + ", analytics_Agepercentage="
				+ analytics_Agepercentage + "]";
	}
	
	
}
