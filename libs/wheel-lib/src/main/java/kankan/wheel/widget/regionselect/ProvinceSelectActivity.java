package kankan.wheel.widget.regionselect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.mrwujay.cascade.R;

import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.regionselect.model.CityWheelModel;
import kankan.wheel.widget.regionselect.model.DistrictWheelModel;
import kankan.wheel.widget.regionselect.model.ProvinceWheelModel;
import kankan.wheel.widget.regionselect.model.Result;
import kankan.wheel.widget.regionselect.service.XmlAreaParserHandler;


/**
 * @author Administrator
 */
public class ProvinceSelectActivity extends Activity implements
        View.OnClickListener, OnWheelChangedListener {

    public static final int SUCESS = 100;

    public static final String RESULT_KEY = "result_key";

    public static final String DEFAULT_SELECT_KEY = "default_select_key";

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private boolean init = true;

    private XmlAreaParserHandler handler;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;

    /**
     * 当前选中的省下的所有城市
     */
    protected String[] cityDatas;

    /**
     * 当前选中的市下的所有地区
     */
    protected String[] areaDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.province_select_lay);
        initWindowLocation();
        setUpViews();
        setUpListener();
        String id = getIntent().getStringExtra(DEFAULT_SELECT_KEY);
        setUpData(id);
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);

        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);

    }

    private void setUpData(String id) {

        handler = XmlUtils.getInstance().parseAreaData(this,id);

        prepareProvinceData();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
                ProvinceSelectActivity.this, mProvinceDatas));
        mViewProvince.setCurrentItem(handler.getmCurrentProvice());

        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();

        init = false;

    }

    /**
     * 准备数据
     *
     * @param
     */
    protected void prepareProvinceData() {

        // 省的数据
        List<ProvinceWheelModel> provinceList = handler.getDataList();
        mProvinceDatas = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
            // 遍历所有省的数据
            mProvinceDatas[i] = provinceList.get(i).getName();

        }

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub

        if (init) {
            return;
        }

        if (wheel == mViewProvince) {

            handler.setmCurrentProvice(newValue);
            handler.setmCurrentCity(0);
            handler.setmCurrentDistrict(0);

            updateCities();
        } else if (wheel == mViewCity) {

            handler.setmCurrentCity(newValue);
            handler.setmCurrentDistrict(0);

            updateAreas();
        } else if (wheel == mViewDistrict) {

            handler.setmCurrentDistrict(newValue);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {

        prepareDistrictData();

        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this,
                areaDatas));

        mViewDistrict.setCurrentItem(handler.getmCurrentDistrict());

    }

    /**
     * 当前选中的所有城市下面的所有地区
     */
    private void prepareDistrictData() {

        // 省的数据
        List<ProvinceWheelModel> provinceList = handler.getDataList();
        List<CityWheelModel> cityList = provinceList.get(
                handler.getmCurrentProvice()).getCityList();

        if (null == cityList || cityList.isEmpty()) {
            areaDatas = new String[0];
            return;
        }

        List<DistrictWheelModel> districtList = cityList.get(
                handler.getmCurrentCity()).getDistrictList();

        if (null == districtList) {
            areaDatas = new String[0];
            return;
        }

        areaDatas = new String[districtList.size()];
        for (int i = 0; i < districtList.size(); i++) {
            areaDatas[i] = districtList.get(i).getName();

        }
    }

    /**
     * 当前选中的省市下面的所有城市
     */
    private void prepareCitysData() {

        // 省的数据
        List<ProvinceWheelModel> provinceList = handler.getDataList();

        List<CityWheelModel> cityList = provinceList.get(
                handler.getmCurrentProvice()).getCityList();

        if (null == cityList || cityList.isEmpty()) {
            cityDatas = new String[0];
            return;
        }

        cityDatas = new String[cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            // 遍历所有市的数据
            cityDatas[i] = cityList.get(i).getName();

        }

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {

        prepareCitysData();

        mViewCity
                .setViewAdapter(new ArrayWheelAdapter<String>(this, cityDatas));

        mViewCity.setCurrentItem(handler.getmCurrentCity());

        updateAreas();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_confirm) {
            Intent intent = new Intent();
            Result result = handler.getSelectResult();
            intent.putExtra(RESULT_KEY, result);
            setResult(SUCESS, intent);
            this.finish();
        } else if (i == R.id.tv_cancel) {
            this.finish();
        }
    }

    // 重写finish（）方法，加入关闭时的动画
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.dialog_exit);
    }

    private void initWindowLocation() {

//		int screenHeight = getWindow().getWindowManager().getDefaultDisplay()
//				.getHeight();// 获取屏幕高度

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // //lp包含了布局的很多信息，通过lp来设置对话框的布局
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        lp.height = (int) dpToPixel(this, 230);// lp高度设置为屏幕的一半
        getWindow().setAttributes(lp);// 将设置好属性的lp应用到对话框

    }
    /**
     * dp转px
     *
     * @param context 任意上下文
     * @param dp
     */
    public float dpToPixel(Context context, float dp) {
        return dp * (getDisplayMetrics(context).densityDpi / 160F);
    }

    public DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }


}
