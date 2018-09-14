package kankan.wheel.widget.regionselect.model;

import java.util.List;

public class CityModel extends Model {

	private List<DistrictModel> districtList;

	public CityModel() {
		super();
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [districtList=" + districtList + "]";
	}

}