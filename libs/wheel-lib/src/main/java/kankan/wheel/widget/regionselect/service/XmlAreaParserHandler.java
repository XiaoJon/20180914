package kankan.wheel.widget.regionselect.service;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.regionselect.model.CityWheelModel;
import kankan.wheel.widget.regionselect.model.DistrictWheelModel;
import kankan.wheel.widget.regionselect.model.ProvinceWheelModel;
import kankan.wheel.widget.regionselect.model.Result;

public class XmlAreaParserHandler extends DefaultHandler {
	private String id;

	private boolean isSelect = false;

	/**
	 * 当前省的名称
	 */
	protected int mCurrentProvice;
	/**
	 * 当前市的名称
	 */
	protected int mCurrentCity;
	/**
	 * 当前区的名称
	 */
	protected int mCurrentDistrict;

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceWheelModel> provinceList = new ArrayList<ProvinceWheelModel>();

	public XmlAreaParserHandler() {

	}

	public List<ProvinceWheelModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	ProvinceWheelModel provinceModel = new ProvinceWheelModel();
	CityWheelModel cityModel = new CityWheelModel();
	DistrictWheelModel districtModel = new DistrictWheelModel();

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {

		String id = attributes.getValue(0);

		if ((null != id) && (null != this.id) && (id.equals(this.id))) {
			isSelect = true;
		}

		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("Province")) {
			provinceModel = new ProvinceWheelModel();
			provinceModel.setId(id);
			provinceModel.setName(attributes.getValue(1));
			provinceModel.setCityList(new ArrayList<CityWheelModel>());
		} else if (qName.equals("City")) {
			cityModel = new CityWheelModel();
			cityModel.setId(id);
			cityModel.setName(attributes.getValue(1));
			cityModel.setDistrictList(new ArrayList<DistrictWheelModel>());
		} else if (qName.equals("Area")) {
			districtModel = new DistrictWheelModel();
			districtModel.setId(id);
			districtModel.setName(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		// 设置选中
		if (isSelect) {
			mCurrentProvice = provinceList.size();

			if (null != provinceModel.getCityList()) {
				mCurrentCity = provinceModel.getCityList().size();

				if (null != cityModel.getDistrictList()) {
					mCurrentDistrict = cityModel.getDistrictList().size();
				}

			}

			isSelect = false;
		}

		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("Area")) {
			cityModel.getDistrictList().add(districtModel);
		} else if (qName.equals("City")) {
			provinceModel.getCityList().add(cityModel);
		} else if (qName.equals("Province")) {
			provinceList.add(provinceModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

	public int getmCurrentProvice() {
		return mCurrentProvice;
	}

	public void setmCurrentProvice(int mCurrentProvice) {
		this.mCurrentProvice = mCurrentProvice;
	}

	public int getmCurrentCity() {
		return mCurrentCity;
	}

	public void setmCurrentCity(int mCurrentCity) {
		this.mCurrentCity = mCurrentCity;
	}

	public int getmCurrentDistrict() {
		return mCurrentDistrict;
	}

	public void setmCurrentDistrict(int mCurrentDistrict) {
		this.mCurrentDistrict = mCurrentDistrict;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Result getSelectResult() {

		Result result = new Result();

		// 省
		ProvinceWheelModel proModel = provinceList.get(mCurrentProvice);
		result.setProvinceID(proModel.getId());
		result.setProvinceName(proModel.getName());

		// 市
		List<CityWheelModel> cityList = proModel.getCityList();
		if (null != cityList && !cityList.isEmpty()) {
			CityWheelModel ciModel = cityList.get(mCurrentCity);
			result.setCityID(ciModel.getId());
			result.setCityName(ciModel.getName());

			// 区
			List<DistrictWheelModel> districtList = ciModel.getDistrictList();
			if (null != districtList && !districtList.isEmpty()) {
				DistrictWheelModel disModel = ciModel.getDistrictList().get(
						mCurrentDistrict);
				result.setDistrictID(disModel.getId());
				result.setDistrictName(disModel.getName());

			}

		}

		return result;
	}
}
