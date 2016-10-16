package dream.anywhere.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;
import dream.anywhere.Receiver.SmsReceiver;

/**
 * Created by SKYMAC on 16/8/31.
 */
public class LoginActivity extends BaseActivity implements BaseInterface {

    //手机登陆的视图
    @ViewInject(R.id.act_login_LinearLayout1)
    private LinearLayout linPhone;
    //账户登陆的视图
    @ViewInject(R.id.act_login_LinearLayout2)
    private LinearLayout linUser;
    //手机登陆的TextView (点击切换背景)
    @ViewInject(R.id.act_login_phoneLoginTv)
    private TextView phoneTv;
    //账户登陆的TextView
    @ViewInject(R.id.act_login_userLoginTv)
    private TextView userTv;

    //账号登陆
    @ViewInject(R.id.act_login_user_usernameEt)
    private EditText userNameTv;
    @ViewInject(R.id.act_login_user_passwordEt)
    private EditText userPassTv;

    //手机登陆
    @ViewInject(R.id.act_login_phone_phoneNumberEt)
    private EditText phoneNumTv;
    @ViewInject(R.id.act_login_phone_phoneCodeEt)
    private EditText phoneCodeTv;
    @ViewInject(R.id.act_login_phone_getCodeBut)
    private Button getCodeBut;

    //用来供注册页面调用 关闭
    public static LoginActivity act;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
        act = this;
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {

    }

    //点击手机号快速登陆后切换视图
    @OnClick(R.id.act_login_phoneLoginTv)
    public void onPhoneClick(View v) {
        linPhone.setVisibility(View.VISIBLE);
        linUser.setVisibility(View.INVISIBLE);
        phoneTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        userTv.setBackgroundColor(Color.parseColor("#EBEBEB"));
    }

    //点击账号密码登陆后切换视图
    @OnClick(R.id.act_login_userLoginTv)
    public void onUserClick(View v) {
        linUser.setVisibility(View.VISIBLE);
        linPhone.setVisibility(View.INVISIBLE);
        userTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        phoneTv.setBackgroundColor(Color.parseColor("#EBEBEB"));
    }

    //页面返回
    @OnClick(R.id.act_login_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    //跳转注册
    @OnClick(R.id.act_login_reg)
    public void onRegClick(View v) {
        startAct(RegActivity.class);
    }

    //账号密码登陆 登陆按钮
    @OnClick(R.id.act_login_user_loginBut)
    public void onUserLoginClick(View v) {
        //账号
        final String unameStr = userNameTv.getText().toString();
        //密码
        final String upassStr = userPassTv.getText().toString();
        //登陆
        UserBean.loginByAccount(unameStr, upassStr, new LogInListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    toastL("恭喜您，登陆成功！");
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("User", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uname", unameStr);
                    editor.putString("upass", upassStr);
                    editor.commit();
                    //将返回的对象放到Application中
                    MyApplication.user = userBean;

//                    HomeActivity.fragments = new ArrayList<>();
//                    HomeActivity.fragments.add(new HomeFragment());
//                    HomeActivity.fragments.add(new OrderFragment_after());
//                    HomeActivity.fragments.add(new MineFragment());
//                    HomeActivity.adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), HomeActivity.fragments);
//                    HomeActivity.vp.removeAllViews();
//                    HomeActivity.adapter.notifyDataSetChanged();
//                    HomeActivity.vp.setAdapter(HomeActivity.adapter);


                    //关闭当前页面
                    finish();
                } else {
                    toastS("用户名或密码错误！");
                    e.printStackTrace();
                }
            }
        });
    }

    //手机验证 获取验证码点击
    @OnClick(R.id.act_login_phone_getCodeBut)
    public void phoneGetCodeClick(View v) {
        String phoneNum = phoneNumTv.getText().toString();
        if (!phoneNum.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        //开启短信广播接收
        SmsReceiver.setSmsListener(new SmsReceiver.SmsListener() {
            @Override
            public void toSms(String text) {
                phoneCodeTv.setText(text);
            }
        });
        //将获取验证码按钮设置成不可点击状态
        getCodeBut.setClickable(false);
        //设置按钮文字颜色
        getCodeBut.setTextColor(Color.parseColor("#626262"));

        if (timer == null) {
            timer = new CountDownTimer(10000, 1000) {
                //此方法自动在UI线程执行
                @Override
                public void onTick(long l) {
                    getCodeBut.setText((l / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    getCodeBut.setClickable(true);
                    getCodeBut.setTextColor(Color.parseColor("#000000"));
                    getCodeBut.setText("发送验证码");
                }
            };
        }
        timer.start();

        BmobSMS.requestSMSCode(act, phoneNum, "注册登陆短信验证", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                if (e == null) {
                    toastS("验证码发送成功");
                } else {
                    toastS("验证码发送失败" + e.toString());
                }
            }
        });
    }

    //手机验证登陆 登陆按钮点击
    @OnClick(R.id.act_login_phone_loginBut)
    public void onPhoneLoginClick(View v) {
        final String phoneNum = phoneNumTv.getText().toString();
        final String phoneCode = phoneCodeTv.getText().toString();
        BmobUser.signOrLoginByMobilePhone(phoneNum, phoneCode, new LogInListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (userBean != null) {
                    toastS("登陆成功");
                    MyApplication.user = userBean;
                    finish();
                } else {
                    logI(phoneNum + "," + phoneCode + "," + e.toString());
                    toastL("验证失败" + e.toString());
                }
            }
        });
    }

    //QQ
    @OnClick(R.id.act_login_qqImg)
    public void onQQClick(View v) {
        startAct(QqLoginActivity.class);
        finish();
    }

}
