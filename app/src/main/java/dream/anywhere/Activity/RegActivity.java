package dream.anywhere.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;
import dream.anywhere.Receiver.SmsReceiver;

/**
 * Created by SKYMAC on 16/8/31.
 */
public class RegActivity extends BaseActivity implements BaseInterface {

    @ViewInject(R.id.act_reg_phoneEt)
    private EditText phoneEt;
    @ViewInject(R.id.act_reg_codeEt)
    private EditText codeEt;
    @ViewInject(R.id.act_reg_passwordEt)
    private EditText passwordEt;
    @ViewInject(R.id.act_reg_checkPasswordEt)
    private EditText checkPasswordEt;
    @ViewInject(R.id.act_reg_getCodeBut)
    private Button getCodeBut;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reg);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    public void InitView() {
        ViewUtils.inject(this);
    }

    @Override
    public void InitData() {

    }

    @Override
    public void InitViewOper() {

    }

    //返回按钮
    @OnClick(R.id.act_reg_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }

    //获取短信验证码
    @OnClick(R.id.act_reg_getCodeBut)
    public void onGetCodeClick(View v) {
        String phoneStr = phoneEt.getText().toString().trim();
        if (!phoneStr.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        //开启短信广播接收
        SmsReceiver.setSmsListener(new SmsReceiver.SmsListener() {
            @Override
            public void toSms(String text) {
                codeEt.setText(text);
            }
        });
        //将获取验证码按钮设置成不可点击状态
        getCodeBut.setClickable(false);
        //设置按钮文字颜色
        getCodeBut.setTextColor(Color.parseColor("#626262"));

        //Bomb 发送验证码
        BmobSMS.requestSMSCode(act, phoneStr, "注册登陆短信验证", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    toastS("验证码已发送");
                } else {
                    toastL("验证码发送失败");
                }
            }
        });
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
                    getCodeBut.setText("获取验证码");
                }
            };
        }
        timer.start();
    }

    //注册按钮
    @OnClick(R.id.act_reg_regBut)
    public void onRegClick(View v) {
        //获取手机号码
        final String phoneStr = phoneEt.getText().toString().trim();
        //验证手机号码格式
        if (!phoneStr.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            toastS("手机号格式有误，请重新输入。");
            return;
        }
        //获取验证码
        String codeStr = codeEt.getText().toString().trim();
        //获取密码
        final String passwordStr = passwordEt.getText().toString().trim();
        //验证密码格式
        if (!passwordStr.matches("^[a-zA-Z]\\w{6,15}$")) {
            toastS("请检查密码格式：以字母开头，长度为7-16位。");
            return;
        }
        //获取再次输入的密码
        String checkPasswordStr = checkPasswordEt.getText().toString().trim();
        //验证两次输入的密码是否一致
        if (!checkPasswordStr.equals(passwordStr)) {
            toastS("两次密码输入不一致！");
            return;
        }
        //验证手机号与验证码是否匹配
        BmobSMS.verifySmsCode(act, phoneStr, codeStr, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    UserBean user = new UserBean();
                    user.setUsername(phoneStr);
                    user.setMobilePhoneNumber(phoneStr);
                    user.setMobilePhoneNumberVerified(true);
                    user.setPassword(passwordStr);
                    //注册
                    user.signUp(new SaveListener<UserBean>() {
                        @Override
                        public void done(UserBean userBean, cn.bmob.v3.exception.BmobException e) {
                            if(e==null){
                                toastL("恭喜您，注册成功！");
//                                SharedPreferences sharedPreferences = RegActivity.this.getSharedPreferences("User",MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("uname",phoneStr);
//                                editor.putString("upass",passwordStr);
//                                editor.commit();
                                //将返回的对象放到Application中
//                                MyApplication.user = userBean;

                                //关闭当前页面
                                finish();
                                //关闭上一层登陆页面
//                                LoginActivity.act.finish();
                            }else{
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    toastL("验证码有误！请重试！");
                    e.printStackTrace();
                }
            }
        });

    }
}
