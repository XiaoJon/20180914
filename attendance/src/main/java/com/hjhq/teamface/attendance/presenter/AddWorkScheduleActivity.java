package com.hjhq.teamface.attendance.presenter;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.AddWorkTimeBean;
import com.hjhq.teamface.attendance.bean.WorkTimeDetailBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.utils.NotifyUtils;
import com.hjhq.teamface.attendance.views.AddWorkScheduleDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_work_schedule", desc = "添加班次")
public class AddWorkScheduleActivity extends ActivityPresenter<AddWorkScheduleDelegate, AttendanceModel> implements View.OnClickListener {

    RadioGroup mRadioGroup;
    com.kyleduo.switchbutton.SwitchButton haveRestTime;
    private int mode = 1;
    boolean isHaveRestTime = true;
    int addOrEdit = 1;
    String id;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addOrEdit = bundle.getInt(Constants.DATA_TAG1);
            id = bundle.getString(Constants.DATA_TAG2);


        }
        initView();
    }

    private void initView() {
        mRadioGroup = viewDelegate.get(R.id.radio_group);
        haveRestTime = viewDelegate.get(R.id.mode_btn);
        if (addOrEdit == AttendanceConstants.TYPE_EDIT) {
            viewDelegate.get(R.id.rl_root).setVisibility(View.INVISIBLE);
            getNetData();
        } else if (addOrEdit == AttendanceConstants.TYPE_ADD) {
            haveRestTime.setChecked(isHaveRestTime);
            viewDelegate.setHasRestTime(isHaveRestTime);
            showRestTime(isHaveRestTime);
        }

    }

    public void getNetData() {
        model.findAttendanceTimeDetail(mContext, id, new ProgressSubscriber<WorkTimeDetailBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(WorkTimeDetailBean workTimeDetailBean) {
                super.onNext(workTimeDetailBean);
                WorkTimeDetailBean.DataBean data = workTimeDetailBean.getData();
                viewDelegate.setName(data.getName());
                mode = TextUtil.parseInt(data.getTimes());
                mRadioGroup.getChildAt(mode - 1).performClick();
                switch (mode) {
                    case 1:
                        viewDelegate.setTime(11, data.getTime1_start());
                        viewDelegate.setTime(12, data.getTime1_end());
                        if (!TextUtils.isEmpty(data.getRest1_start()) && !TextUtils.isEmpty(data.getRest1_end())) {
                            viewDelegate.setTime(13, data.getRest1_start());
                            viewDelegate.setTime(14, data.getRest1_end());
                            haveRestTime.setChecked(true);
                            isHaveRestTime = true;
                            viewDelegate.setHasRestTime(true);
                        } else {
                            haveRestTime.setChecked(false);
                            isHaveRestTime = false;
                            viewDelegate.setHasRestTime(false);
                        }
                        showRestTime(isHaveRestTime);
                        break;
                    case 2:
                        viewDelegate.setTime(21, data.getTime1_start());
                        viewDelegate.setTime(22, data.getTime1_end());
                        viewDelegate.setTime(23, data.getTime2_start());
                        viewDelegate.setTime(24, data.getTime2_end());
                        break;
                    case 3:
                        viewDelegate.setTime(31, data.getTime1_start());
                        viewDelegate.setTime(32, data.getTime1_end());
                        viewDelegate.setTime(33, data.getTime2_start());
                        viewDelegate.setTime(34, data.getTime2_end());
                        viewDelegate.setTime(35, data.getTime3_start());
                        viewDelegate.setTime(36, data.getTime3_end());
                        break;
                }
                viewDelegate.setVisibility(R.id.rl_root, View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
        String name = viewDelegate.getName();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(mContext, "请输入班次名称");
            return true;
        }
        AddWorkTimeBean bean = new AddWorkTimeBean();
        bean.setName(name);
        bean.setTimes(mode + "");
        switch (mode) {
            case 1:
                dataMode1(bean);
                break;
            case 2:
                dataMode2(bean);
                break;
            case 3:
                dataMode3(bean);
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(id)) {
            model.saveAttendanceTime(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "保存成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            bean.setId(id);
            model.updateAttendanceTime(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "保存成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void dataMode1(AddWorkTimeBean bean) {
        String time11 = viewDelegate.getTime(0);
        String time12 = viewDelegate.getTime(1);
        String time13 = viewDelegate.getTime(2);
        String time14 = viewDelegate.getTime(3);

        if (isHaveRestTime) {
            bean.setRest1Start(time13);
            bean.setRest1End(time14);
            /*if (TextUtils.isEmpty(time13) || TextUtils.isEmpty(time14)) {
                ToastUtils.showToast(mContext, "请选择休息开始或结束时间");
                return;
            } else {
                bean.setRest1Start(time13);
                bean.setRest1End(time14);
            }*/
        } else {
            bean.setRest1Start("");
            bean.setRest1End("");
        }

        bean.setTime1Start(time11);
        bean.setTime1End(time12);
        bean.setTime2Start("");
        bean.setTime2End("");
        bean.setTime3Start("");
        bean.setTime3End("");


        bean.setTime1EndStatus("");
        bean.setTime2EndStatus("");
        bean.setTime3EndStatus("");


        bean.setTime1StartLimit("");
        bean.setTime1EndLimit("");
        bean.setTime2StartLimit("");
        bean.setTime2EndLimit("");
        bean.setTime3StartLimit("");
        bean.setTime3EndLimit("");


        bean.setTotalWorkingHours(viewDelegate.getTotalTimeString());
        bean.setAttendanceTime(time11 + "-" + time12 + ";");
    }

    private void dataMode2(AddWorkTimeBean bean) {
        String time11 = viewDelegate.getTime(4);
        String time12 = viewDelegate.getTime(5);
        String time13 = viewDelegate.getTime(6);
        String time14 = viewDelegate.getTime(7);
        bean.setTime1Start(time11);
        bean.setTime1End(time12);
        bean.setTime2Start(time13);
        bean.setTime2End(time14);
        bean.setTime3Start("");
        bean.setTime3End("");


        bean.setRest1Start("");
        bean.setRest1End("");


        bean.setTime1EndStatus("");
        bean.setTime2EndStatus("");
        bean.setTime3EndStatus("");


        bean.setTime1StartLimit("");
        bean.setTime1EndLimit("");
        bean.setTime2StartLimit("");
        bean.setTime2EndLimit("");
        bean.setTime3StartLimit("");
        bean.setTime3EndLimit("");


        bean.setTotalWorkingHours(viewDelegate.getTotalTimeString());
        bean.setAttendanceTime(time11 + "-" + time12 + ";" + time13 + "-" + time14 + ";");
    }

    private void dataMode3(AddWorkTimeBean bean) {
        String time11 = viewDelegate.getTime(8);
        String time12 = viewDelegate.getTime(9);
        String time13 = viewDelegate.getTime(10);
        String time14 = viewDelegate.getTime(11);
        String time15 = viewDelegate.getTime(12);
        String time16 = viewDelegate.getTime(13);

        bean.setTime1Start(time11);
        bean.setTime1End(time12);
        bean.setTime2Start(time13);
        bean.setTime2End(time14);
        bean.setTime3Start(time15);
        bean.setTime3End(time16);


        bean.setRest1Start("");
        bean.setRest1End("");


        bean.setTime1EndStatus("");
        bean.setTime2EndStatus("");
        bean.setTime3EndStatus("");


        bean.setTime1StartLimit("");
        bean.setTime1EndLimit("");
        bean.setTime2StartLimit("");
        bean.setTime2EndLimit("");
        bean.setTime3StartLimit("");
        bean.setTime3EndLimit("");


        bean.setTotalWorkingHours(viewDelegate.getTotalTimeString());
        bean.setAttendanceTime(time11 + "-" + time12 + ";" + time13 + "-" + time14 + ";" + time15 + "-" + time16 + ";");
    }

    @Override
    protected void bindEvenListener() {
        haveRestTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: 2018/6/8 控制显示/隐藏 
                if (isChecked) {
                    isHaveRestTime = true;
                } else {
                    isHaveRestTime = false;
                }
                viewDelegate.setHasRestTime(isHaveRestTime);
                showRestTime(isHaveRestTime);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
                if (checkedId == R.id.rb1) {
                    mode = 1;
                } else if (checkedId == R.id.rb2) {
                    mode = 2;
                } else if (checkedId == R.id.rb3) {
                    mode = 3;
                }
                viewDelegate.switchType(mode);
            }
        });
        //一天一班
        viewDelegate.get(R.id.rl11).setOnClickListener(this);
        viewDelegate.get(R.id.rl12).setOnClickListener(this);
        viewDelegate.get(R.id.rl13).setOnClickListener(this);
        viewDelegate.get(R.id.rl14).setOnClickListener(this);
        //一天两班
        viewDelegate.get(R.id.rl21).setOnClickListener(this);
        viewDelegate.get(R.id.rl22).setOnClickListener(this);
        viewDelegate.get(R.id.rl23).setOnClickListener(this);
        viewDelegate.get(R.id.rl24).setOnClickListener(this);
        //一天三班
        viewDelegate.get(R.id.rl31).setOnClickListener(this);
        viewDelegate.get(R.id.rl32).setOnClickListener(this);
        viewDelegate.get(R.id.rl33).setOnClickListener(this);
        viewDelegate.get(R.id.rl34).setOnClickListener(this);
        viewDelegate.get(R.id.rl35).setOnClickListener(this);
        viewDelegate.get(R.id.rl36).setOnClickListener(this);
        super.bindEvenListener();
    }

    private void showRestTime(boolean isHaveRestTime) {
        if (isHaveRestTime) {
            viewDelegate.get(R.id.rl13).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.rl14).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.line4).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.line3).setVisibility(View.VISIBLE);
        } else {
            viewDelegate.get(R.id.rl13).setVisibility(View.GONE);
            viewDelegate.get(R.id.rl14).setVisibility(View.GONE);
            viewDelegate.get(R.id.line4).setVisibility(View.GONE);
            viewDelegate.get(R.id.line3).setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
        if (v.getId() == R.id.rl11) {
            selectTime(11, viewDelegate.getTime(0));
        } else if (v.getId() == R.id.rl12) {
            selectTime(12, viewDelegate.getTime(1));
        } else if (v.getId() == R.id.rl13) {
            selectTime(13, viewDelegate.getTime(2));
        } else if (v.getId() == R.id.rl14) {
            selectTime(14, viewDelegate.getTime(3));
        } else if (v.getId() == R.id.rl21) {
            selectTime(21, viewDelegate.getTime(0));
        } else if (v.getId() == R.id.rl22) {
            selectTime(22, viewDelegate.getTime(5));
        } else if (v.getId() == R.id.rl23) {
            selectTime(23, viewDelegate.getTime(6));
        } else if (v.getId() == R.id.rl24) {
            selectTime(24, viewDelegate.getTime(7));
        } else if (v.getId() == R.id.rl31) {
            selectTime(31, viewDelegate.getTime(8));
        } else if (v.getId() == R.id.rl32) {
            selectTime(32, viewDelegate.getTime(9));
        } else if (v.getId() == R.id.rl33) {
            selectTime(33, viewDelegate.getTime(10));
        } else if (v.getId() == R.id.rl34) {
            selectTime(34, viewDelegate.getTime(11));
        } else if (v.getId() == R.id.rl35) {
            selectTime(35, viewDelegate.getTime(12));
        } else if (v.getId() == R.id.rl36) {
            selectTime(36, viewDelegate.getTime(13));
        }
    }

    public void selectTime(int i, String time) {

        NotifyUtils.showTimeSelectMenu(mContext, viewDelegate.getRootView(), time, new NotifyUtils.OnTimeSelectedListener() {
            @Override
            public void selected(String time) {
                viewDelegate.setTime(i, time);

            }
        });
    }
}
