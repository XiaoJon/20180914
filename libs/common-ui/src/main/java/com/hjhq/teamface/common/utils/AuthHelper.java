package com.hjhq.teamface.common.utils;

import com.hjhq.teamface.basis.bean.ModuleAuthBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthBean;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.SingleInstance;
import com.hjhq.teamface.common.CommonModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import rx.Subscriber;

/**
 * @author Administrator
 * @date 2017/10/17
 * Describe:权限获取与判断
 */

public class AuthHelper extends SingleInstance {
    private static CommonModel model = new CommonModel();
    //内存中缓存功能权限
    public static Map<String, ModuleFunctionBean> authMap = new HashMap<>();

    public static AuthHelper getInstance() {
        return (AuthHelper) SingleInstance.getInstance(AuthHelper.class.getName());
    }

    /**
     * 获取功能权限,用于基类是ActivityPresenter的界面
     *
     * @param activity
     * @param moduleBean 模块名
     * @param listener   获取权限后的回调
     */

    public void getModuleFunctionAuth(@Nonnull ActivityPresenter activity, @Nonnull String moduleBean, @Nonnull InitialDataCompleteListener listener) {
//        ModuleFunctionBean moduleFunctionBean = authMap.get(moduleBean);
//        if (moduleFunctionBean != null && moduleFunctionBean.getData() != null && moduleFunctionBean.getData().size() > 0) {
//            if (listener != null) {
//                listener.complete(moduleFunctionBean);
//                return;
//            }
//        }

        model.getMoudleFunctionAuth(activity, moduleBean, new ProgressSubscriber<ModuleFunctionBean>(activity) {
            @Override
            public void onNext(ModuleFunctionBean moduleFunctionBean) {
                super.onNext(moduleFunctionBean);
                authMap.put(moduleBean, moduleFunctionBean);
                if (listener != null) {
                    listener.complete(moduleFunctionBean);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (listener != null) {
                    listener.error();
                }
            }
        });
    }

    /**
     * 权限判断
     *
     * @param moduleBean
     * @param authId
     * @return
     */
    public boolean checkFunctionAuth(@Nonnull String moduleBean, @Nonnull String authId) {
        ModuleFunctionBean bean = authMap.get(moduleBean);
        if (bean == null || bean.getData().size() <= 0) {
            return false;
        }
        for (int i = 0; i < bean.getData().size(); i++) {
            if (authId.equals(bean.getData().get(i).getAuth_code())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证功能权限
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void getAuthByBean(RxAppCompatActivity mActivity, String bean, String dataId, Subscriber<ViewDataAuthBean> s) {
        model.getAuthByBean(mActivity, bean, dataId + "", s);
    }

    /**
     * 验证模块权限
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void checkAuthByModule(RxAppCompatActivity mActivity, String bean, Subscriber<ModuleAuthBean> s) {
        model.getAuthByModule(mActivity, bean, s);
    }

    public interface InitialDataCompleteListener {
        void complete(ModuleFunctionBean moduleFunctionBean);

        void error();
    }
}
