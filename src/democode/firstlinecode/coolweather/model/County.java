package democode.firstlinecode.coolweather.model;

public class County {

	private int id;

	private String countyName;

	private String countyCode;

	/**
	 * 这里的值很多实际上都是重复的,因为有很多的市.
	 */
	private int cityId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public County(String countyName, String countyCode, int cityId) {
		super();
		this.countyName = countyName;
		this.countyCode = countyCode;
		this.cityId = cityId;
	}

	public County() {
		super();
	}
	
}