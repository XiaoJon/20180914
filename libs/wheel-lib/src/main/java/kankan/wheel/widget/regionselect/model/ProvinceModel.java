package kankan.wheel.widget.regionselect.model;

import java.util.List;

public class ProvinceModel extends Model {

	private List<CityModel> cityList;

	public ProvinceModel() {
		super();
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

}