package kankan.wheel.widget.regionselect.service;

import kankan.wheel.widget.regionselect.model.ParentModel;
import kankan.wheel.widget.regionselect.model.ResultForGuild;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlJobParserHandler extends DefaultHandler {

	/**
	 * 省市地区选中的位置
	 */
	private int selectPostion = 0;

	private String id;

	private boolean isSelect = false;

	/**
	 * 存储所有的解析对象
	 */
	private List<ParentModel> jobList = new ArrayList<ParentModel>();

	public XmlJobParserHandler() {

	}

	public List<ParentModel> getDataList() {
		return jobList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	ParentModel parentModel = new ParentModel();

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		String id = attributes.getValue(0);
		if ((null != id) && (null != this.id) && (id.equals(this.id))) {
			isSelect = true;
		}

		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("industry")) {
			parentModel = new ParentModel();
			parentModel.setId(id);
			parentModel.setName(attributes.getValue(1));

		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (isSelect) {
			selectPostion = jobList.size();
			isSelect = false;
			// LogUtils.e(cityModel.toString()+"----------------");
			// LogUtils.e(provinceModel.getChildList().size()+"----------------");
			// LogUtils.e(cityModel.toString()+"----------------");
		}

		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("industry")) {
			jobList.add(parentModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

	/**
	 * 获取选中的结果
	 * 
	 * @return
	 */
	public ResultForGuild getSelectResult() {

		ResultForGuild result = new ResultForGuild();

		// 父职业
		ParentModel proModel = jobList.get(selectPostion);
		result.setJobID(proModel.getId());
		result.setJobName(proModel.getName());

		return result;
	}

	public int getSelectPostion() {
		return selectPostion;
	}

	public void setSelectPostion(int selectPostion) {
		this.selectPostion = selectPostion;
	}

	public void setId(String id) {
		this.id = id;
	}

}
