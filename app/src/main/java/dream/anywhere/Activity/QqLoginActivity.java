package dream.anywhere.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * 第三方(QQ)登录
 *
 */
@SuppressLint("HandlerLeak")
public class QqLoginActivity extends Activity implements OnClickListener {
    // 用户信息
    private TextView mUserInfo;
    // 用户头像
    private ImageView mUserLogo;
    // 用户登录
    private Button mNewLoginButton;
    // 返回用户信息
    private TextView backInfo;
    private UserInfo mInfo;
    private Tencent mTencent;
    public QQAuth mQQAuth;
    // 申请的id
    public String mAppid = "222222";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);
        // 初始化视图
        initView();
    }

    public void initView() {
        // 从当前加载的布局文件转换的视图中 获取TextView的引用
        mUserInfo = (TextView) findViewById(R.id.user_nickname);
        // 从当前加载的布局文件转换的视图中 获取ImageView的引用
        mUserLogo = (ImageView) findViewById(R.id.user_logo);
        // 从当前加载的布局文件转换的视图中 获取Button的引用
        mNewLoginButton = (Button) findViewById(R.id.new_login_btn);
        mNewLoginButton.setOnClickListener(this);
        // 从当前加载的布局文件转换的视图中 获取TextView的引用
        backInfo = (TextView) findViewById(R.id.user_callback);
        // Tencent类是SDK的主要实现类，通过此访问腾讯开放的OpenAPI。
        mQQAuth = QQAuth.createInstance(mAppid, getApplicationContext());
        // 实例化
        mTencent = Tencent.createInstance(mAppid, getApplicationContext());
    }

    /**
     * 设置子线程下载的图片，设置textview显示用户名昵称
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mUserInfo.setVisibility(View.VISIBLE);
                mUserInfo.setText(msg.getData().getString("nickname"));
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mUserLogo.setImageBitmap(bitmap);
                mUserLogo.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 解析返回的json数据
     */
    private void updateUserInfo() {
        // if (mQQAuth != null && mQQAuth.isSessionValid()) {
        IUiListener listener = new IUiListener() {
            @Override
            public void onError(UiError e) {
                Toast.makeText(QqLoginActivity.this, "异常" + e.toString(), Toast.LENGTH_SHORT).show();
            }

            // 所有数据在这里，已经获取成功
            @Override
            public void onComplete(final Object response) {
                JSONObject json = (JSONObject) response;

                Log.i("after", "返回数据位" + json);
                // 昵称
                // 头像 暂时锁定这个功能

                String id = null;
                String nickName = null;
                BmobFile bFile = null;
                try {
                    id = json.getString("openid");
                    nickName = json.getString("nickname");
                    bFile = new BmobFile(new File(json.getString("figureurl_qq_2")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BmobQuery<UserBean> query = new BmobQuery<>();
                query.addWhereEqualTo("username", id);
                final String finalId = id;
                final String finalNickName = nickName;
                final BmobFile finalBFile = bFile;
                query.findObjects(new FindListener<UserBean>() {
                    @Override
                    public void done(List<UserBean> list, BmobException e) {
                        if (list.size() > 0) {
                            //登陆
                            MyApplication.user = list.get(0);
//                            startActivity(new Intent(QqLoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            //注册
                            UserBean u = new UserBean();
                            u.setUsername(finalId);
                            u.setNickName(finalNickName);
                            u.setUserIcon(finalBFile);
                            u.save(new SaveListener() {
                                @Override
                                public void done(Object o, BmobException e) {
                                    if (e == null) {
                                        MyApplication.user = (UserBean) o;
//                                        startActivity(new Intent(QqLoginActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });


//                startActivity(new Intent(QqLoginActivity.this, HomeActivity.class));
//                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(QqLoginActivity.this, "用户取消".toString(), Toast.LENGTH_SHORT).show();
            }
        };
        mInfo = new UserInfo(this, mTencent.getQQToken());
        mInfo.getUserInfo(listener);
        Log.i("after", "mInfo.getUserInfo(listener)");
    }

    /**
     * 开启线程 获取头像
     */
    class MyImgThread implements Runnable {
        private String imgPath;
        private Bitmap bitmap;

        public MyImgThread(String imgpath) {
            this.imgPath = imgpath;
        }

        @Override
        public void run() {
            bitmap = getImgBitmap(imgPath);
            Message msg = new Message();
            msg.obj = bitmap;
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 根据头像的url 获取bitmap
     */
    public Bitmap getImgBitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL myFileUrl = new URL(imageUri);
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                conn.disconnect();
                is.close();
                is.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 登录开始
     */
    public void onClickLogin() {
        // 登录
        if (!mQQAuth.isSessionValid()) {
            // 实例化回调接口
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    Log.i("after", "数据1" + values);
                    // 更新author数据
                    String token;
                    try {
                        token = values.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = values.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = values.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                            mTencent.setAccessToken(token, expires);
                            mTencent.setOpenId(openId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateUserInfo();
                    // updateLoginButton();
                    // if (mQQAuth != null) {// 设置button按钮
                    // mNewLoginButton.setTextColor(Color.BLUE);
                    // mNewLoginButton.setText("登录");
                    // }
                }
            };
            // 开始调起接口
            mTencent.loginWithOEM(this, "all", listener, "10000144", "10000144", "xxxx");
        } else {
            // 注销登录
            mQQAuth.logout(this);
            updateUserInfo();
            // updateLoginButton();
            mNewLoginButton.setTextColor(Color.RED);
            mNewLoginButton.setText("退出帐号");
        }
    }

    /**
     * 调用SDK封装好的借口，需要传入回调的实例 会返回服务器的消息
     */
    private class BaseUiListener implements IUiListener {
        /**
         * 成功
         */
        @Override
        public void onComplete(Object response) {
            backInfo.setText(response.toString());
            doComplete((JSONObject) response);
        }

        /**
         * 处理返回的消息 比如把json转换为对象什么的
         *
         * @param values
         */
        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(QqLoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(QqLoginActivity.this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    // 点击开始获取信息
    @Override
    public void onClick(View v) {
        // 当点击登录按钮
        if (v == mNewLoginButton) {
            onClickLogin();
        }
    }
}
