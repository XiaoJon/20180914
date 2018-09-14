package kankan.wheel.widget.regionselect;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.regionselect.service.XmlAreaParserHandler;
import kankan.wheel.widget.regionselect.service.XmlJobParserHandler;

/**
 * @author Administrator
 */
public class XmlUtils {

	private XmlUtils() {
		// TODO Auto-generated constructor stub
	}

	private static class SingletonHolder {
		private static final XmlUtils INSTANCE = new XmlUtils();
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static XmlUtils getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 解析数据职业数据
	 * 
	 * @param selectID
	 * @return
	 */
	public XmlJobParserHandler parseJobData(Context context,String selectID) {

		XmlJobParserHandler handler = null;

		try {

			handler = new XmlJobParserHandler();

			AssetManager asset = context.getAssets();

			InputStream input = asset.open("industry.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();

			handler.setId(selectID);

			parser.parse(input, handler);

			input.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return handler;

	}

	/**
	 * 解析省市数据
	 * 
	 * @param selectID
	 * @return
	 */
	public XmlAreaParserHandler parseAreaData(Context context,String selectID) {

		XmlAreaParserHandler handler = null;

		try {
			handler = new XmlAreaParserHandler();

			AssetManager asset = context.getAssets();

			InputStream input = asset.open("ProvinceAndCity.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();

			handler.setId(selectID);

			parser.parse(input, handler);

			input.close();
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();

		}

		return handler;

	}

}
