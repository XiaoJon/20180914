package kankan.wheel.widget.regionselect.model;

import java.io.Serializable;

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2164900716684507296L;

	private String provinceName;

	private String provinceID;

	private String cityName;

	private String cityID;

	private String districtName;

	private String districtID;

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictID() {
		return districtID;
	}

	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}

	@Override
	public String toString() {
		return "Result [provinceName=" + provinceName + ", provinceID="
				+ provinceID + ", cityName=" + cityName + ", cityID=" + cityID
				+ ", districtName=" + districtName + ", districtID="
				+ districtID + "]";
	}

}
