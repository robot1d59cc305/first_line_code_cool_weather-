package democode.firstlinecode.coolweather.model;

public class City {

	private int id;

	private String cityName;

	private String cityCode;

	/**
	 * 这里的值很多实际上都是有重复的因为有很多的省.
	 */
	private int provinceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public City(String cityName, String cityCode, int provinceId) {
		super();
		this.cityName = cityName;
		this.cityCode = cityCode;
		this.provinceId = provinceId;
	}

	public City() {
		super();
	}
	
}