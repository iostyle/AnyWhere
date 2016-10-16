package dream.anywhere.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/1.
 */
public class SettingPasswordActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_setting_password_oldEt)
    private EditText oldEt;
    @ViewInject(R.id.act_setting_password_newEt)
    private EditText newEt;
    @ViewInject(R.id.act_setting_password_checkEt)
    private EditText checkEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_password);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(act);
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {

    }


    @OnClick(R.id.act_setting_password_but)
    public void onClick(View v) {
        String oldStr = oldEt.getText().toString();
        String newStr = newEt.getText().toString();
        if (!newStr.matches("^[a-zA-Z]\\w{6,15}$")) {
            toastS("请检查密码格式：以字母开头，长度为7-16位。");
            return;
        }
        String checkStr = checkEt.getText().toString();
        if (!newStr.equals(checkStr)) {
            toastS("两次输入的密码不一致！");
            return;
        }
        MyApplication.user.updateCurrentUserPassword(oldStr, newStr, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastS("修改成功，请重新登陆");

                    //注销当前用户
                    MyApplication.user = null;
//                    SharedPreferences sharedPreferences = SettingPasswordActivity.this.getSharedPreferences("User", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.remove("uname");
//                    editor.remove("upass");
//                    editor.commit();
                    BmobUser.logOut();

                    //递归关闭
                    SettingActivity.settingAct.finish();
                    startAct(LoginActivity.class);
                    finish();
                } else {
                    toastL("修改失败" + e.toString());
                }
            }
        });
    }

    @OnClick(R.id.act_setting_password_backLinearLayout)
    public void onBackClick(View v){
        finish();
    }
}
