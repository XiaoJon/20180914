package kankan.wheel.widget.regionselect.model;

import java.util.List;


public class ProvinceWheelModel extends WheelModel{

	private List<CityWheelModel> cityList;

	public ProvinceWheelModel() {
		super();
	}

	public List<CityWheelModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityWheelModel> cityList) {
		this.cityList = cityList;
	}

}
