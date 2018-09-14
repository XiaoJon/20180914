package kankan.wheel.widget.regionselect.model;

import java.util.List;


public class CityWheelModel extends WheelModel {

	private List<DistrictWheelModel> districtList;

	public CityWheelModel() {
		super();
	}

	public List<DistrictWheelModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictWheelModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityWheelModel [districtList=" + districtList + "]";
	}

}
