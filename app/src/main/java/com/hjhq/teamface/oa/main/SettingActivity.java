package com.hjhq.teamface.oa.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseTitleActivity;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.provider.MyFileProvider;
import com.hjhq.teamface.basis.util.FileUtils;
import com.hjhq.teamface.basis.util.SystemFuncUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.oa.login.logic.SettingHelper;
import com.hjhq.teamface.util.CommonUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.File;

import butterknife.Bind;

/**
 * 设置
 *
 * @author lx
 * @date 2017/6/15
 */
@RouteNode(path = "/setting", desc = "设置")
public class SettingActivity extends BaseTitleActivity {
    @Bind(R.id.ll_change_password)
    LinearLayout llChangePassword;
    @Bind(R.id.ll_change_phone)
    LinearLayout llChangePhone;
    @Bind(R.id.ll_change_language)
    LinearLayout llChangeLanguage;
    @Bind(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.tv_clear_cache)
    TextView tvClearCache;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.sbtn_weixin)
    SwitchButton switchButtonWeiXin;
    @Bind(R.id.size_limit)
    SwitchButton switchButtonSizeLimit;
    @Bind(R.id.ll_weixin)
    View llWeiXin;
    @Bind(R.id.ll_version)
    LinearLayout llVersion;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getChildView() {
        return R.layout.activity_setting;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        TextUtil.setText(tvPhone, SPHelper.getUserAccount());
    }

    @Override
    protected void initView() {
        super.initView();
        setActivityTitle("设置");
        TextUtil.setText(tvPhone, SPHelper.getUserAccount());
    }

    @Override
    protected void setListener() {
        setOnClicks(llChangePassword, llChangeLanguage, llChangePhone, llClearCache, tvLogout, llVersion);
        switchButtonSizeLimit.setChecked(SPHelper.getSizeLimitState(true));
        SPHelper.setSizeLimitState(switchButtonSizeLimit.isChecked());
        llWeiXin.setOnClickListener(v -> ToastUtils.showError(mContext, "敬请期待"));
        switchButtonSizeLimit.setOnCheckedChangeListener((buttonView, isChecked) -> openOrCloseSizeLimit(isChecked));
    }

    /**
     * .
     * 非WiFi网络环境时上传文件大小限制开关
     */
    private void openOrCloseSizeLimit(boolean check) {
        if (switchButtonSizeLimit.getTag(R.id.size_limit) != null) {
            switchButtonSizeLimit.setTag(R.id.size_limit, null);
            return;
        }
        if (check) {
            DialogUtils.getInstance().sureOrCancel(this,
                    "提示",
                    "开启后大小超过10M的文件仅在Wifi连接时上传/下载",
                    getContainer(), "确定", "取消", new DialogUtils.OnClickSureOrCancelListener() {
                        @Override
                        public void clickSure() {
                            SPHelper.setSizeLimitState(true);
                        }

                        @Override
                        public void clickCancel() {
                            switchButtonSizeLimit.setTag(R.id.size_limit, true);
                            switchButtonSizeLimit.setChecked(false);
                            SPHelper.setSizeLimitState(false);
                        }
                    });
        } else {
            DialogUtils.getInstance().sureOrCancel(this, "提示",
                    "关闭后，非wifi环境上传/下载大于10M的文件时将不再显示流量提醒",
                    getContainer(), "确定", "取消", new DialogUtils.OnClickSureOrCancelListener() {
                        @Override
                        public void clickSure() {
                            SPHelper.setSizeLimitState(false);
                        }

                        @Override
                        public void clickCancel() {
                            switchButtonSizeLimit.setTag(R.id.size_limit, true);
                            switchButtonSizeLimit.setChecked(true);
                            SPHelper.setSizeLimitState(true);
                        }
                    });
        }
    }

    @Override
    protected void initData() {
        tvClearCache.setText(SettingHelper.getCacheSize());
        TextUtil.setText(tvVersion, String.format(getResources().getString(R.string.version_name), SystemFuncUtils.getVersionName(SettingActivity.this)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_phone:
                CommonUtil.startActivtiy(mContext, ChangePhoneActivity.class);
                break;
            case R.id.ll_change_language:
                ToastUtils.showError(mContext, "敬请期待");
                break;
            case R.id.ll_change_password:
                //修改密码
                changePassword();
                break;
            case R.id.ll_clear_cache:
                //清理缓存
                clearCache();
                break;
            case R.id.tv_logout:
                //退出登录
                logout();
                break;
            case R.id.ll_version:
                //检查更新
                checkNewVersion();
                break;
            default:
                break;
        }
    }

    /**
     * 修改手机密码
     */
    private void changePassword() {
        CommonUtil.startActivtiy(this, ChangePasswordActivity.class);
    }


    /**
     * 清理缓存
     */
    private void clearCache() {
        DialogUtils.getInstance().sureOrCancel(this, "您确认清空缓存？", "", getContainer(), () -> {
                    boolean bl = SettingHelper.clearCache();
                    showToast(bl ? "清理成功" : "清理失败");
                    if (bl) {
                        tvClearCache.setText("(" + SettingHelper.getCacheSize() + ")");
                    }
                }
        );
    }

    /**
     * 退出登录
     */
    private void logout() {
        DialogUtils.getInstance().sureOrCancel(mContext, "", "确认退出登录?",
                tvLogout.getRootView(), new DialogUtils.OnClickSureListener() {
                    @Override
                    public void clickSure() {
                        SettingHelper.logout();
                    }
                });

    }

    /**
     * 检查版本
     */
    private void checkNewVersion() {
        DialogUtils.getInstance().sureOrCancel(mContext, "",
                "当前版本为:" + SystemFuncUtils.getVersionName(SettingActivity.this),
                tvLogout.getRootView(), new DialogUtils.OnClickSureListener() {
                    @Override
                    public void clickSure() {
                        File file = new File(JYFileHelper.getFileDir(SettingActivity.this, Constants.PATH_DOWNLOAD), MsgConstant.NEW_VERSION_APK_ID + "12.apk");
                        FileUtils.installApk(mContext, SystemFuncUtils.getPackageName(mContext), file.getAbsolutePath());
                    }
                });
    }

    //安装应用的流程
    private void installProcess(File apk) {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                DialogUtils.getInstance().sureOrCancel(this, "", "安装应用需要打开未知来源权限，请去设置中开启权限",
                        llVersion, new DialogUtils.OnClickSureListener() {
                            @Override
                            public void clickSure() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startInstallPermissionSettingActivity();
                                }
                            }
                        });
                return;
            }
        }
        //有权限，开始安装应用程序
        installApk(apk);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, Constants.REQUEST_CODE9);
    }

    //安装应用
    private void installApk(File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        } else {//Android7.0之后获取uri要用contentProvider
            Uri uri = MyFileProvider.getUriForFile(getBaseContext(), Constants.FILE_PROVIDER, apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
    }

    /**
     * 绑定或解绑
     *
     * @param isCheck
     * @param switchButton
     */
    private void bindOrunbind(boolean isCheck, SwitchButton switchButton) {
        String title = "确定要解除绑定";
        if (isCheck) {
            title = "TeamFace 想要打开微信";
        }
        DialogUtils.getInstance().sureOrCancel(this, title, "", getContainer(), "确定", "取消", new DialogUtils.OnClickSureOrCancelListener() {
            @Override
            public void clickSure() {
            }

            @Override
            public void clickCancel() {
                switchButton.setChecked(false);
            }
        });

    }

}
