package com.hjhq.teamface.statistic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.hjhq.teamface.basis.util.ToastUtils;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class TestActivity extends AppCompatActivity {
    int pwdLength = 6;
    String pattern;


    private EditText etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_test_activity);
        etPwd = findViewById(R.id.et_pwd);

    }

    public void test(View view) {
        UIRouter.getInstance().openUri(this, "DDComp://login/login", null);
    }

    public boolean isDight(String str) {
        boolean isDigit = false;
        for (int i = 0; i < str.length(); i++) {
            //用char包装类中的判断数字的方法判断每一个字符
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            }
        }
        return isDigit;
    }

    public boolean isLowerCase(String str) {
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        return isUpperCase;
    }

    public boolean isUpperCase(String str) {
        boolean isLowerCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            }
        }
        return isLowerCase;
    }

    public boolean isSpecialChar(String str) {
        String t = "~`@#$%^&*-_=+|\\?/()<>[]{}\",.;'!";
        boolean ist = false;
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < t.length(); j++) {
                if (str.charAt(i) == t.charAt(j)) {
                    ist = true;
                }
            }
        }
        return ist;
    }

    /**
     * 包含字母和数字
     *
     * @param view
     */
    public void click1(View view) {
        String pwd = etPwd.getText().toString().trim();
        boolean upperCase = (isUpperCase(pwd) || isLowerCase(pwd)) && isDight(pwd);

        String regex = "^[a-zA-Z0-9]+$";
        pwd.matches(regex);
        ToastUtils.showToast(this, upperCase + "" + pwd.matches(regex));
    }

    /**
     * 字母、数字和特殊字符
     *
     * @param view
     */
    public void click2(View view) {
        String pwd = etPwd.getText().toString().trim();
        boolean upperCase = (isUpperCase(pwd) || isLowerCase(pwd)) && isDight(pwd) && isSpecialChar(pwd);

        boolean validation = pwd.matches("^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$");
        ToastUtils.showToast(this, validation + " -" + upperCase);
    }

    /**
     * 数字和大小写字母
     *
     * @param view
     */
    public void click3(View view) {
        String pwd = etPwd.getText().toString().trim();
        boolean upperCase = isUpperCase(pwd) && isLowerCase(pwd) && isDight(pwd);

        String regex = "^[a-zA-Z0-9]+$";
        ToastUtils.showToast(this, upperCase + "" + pwd.matches(regex));
    }

    /**
     * 数字、大小写字母和特殊字符
     *
     * @param view
     */
    public void click4(View view) {
        String pwd = etPwd.getText().toString().trim();
        boolean upperCase = isUpperCase(pwd) && isLowerCase(pwd) && isDight(pwd) && isSpecialChar(pwd);

        String regex = "^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$";
        ToastUtils.showToast(this, upperCase + "" + pwd.matches(regex));
    }

    /**
     * 默认
     *
     * @param view
     */
    public void click5(View view) {
        String pwd = etPwd.getText().toString().trim();
        if (pwd.length()<6 || pwd.length()>16){
            ToastUtils.showError(this,"长度异常");
            return ;
        }
        boolean upperCase = isUpperCase(pwd) || isLowerCase(pwd) || isDight(pwd);

        String regex = "^[a-zA-Z0-9~`@#$%^&*-_=+|\\?/()<>\\[\\]\\{\\}\",.;'!]+$";
        ToastUtils.showToast(this, upperCase + "" + pwd.matches(regex));
    }
}
