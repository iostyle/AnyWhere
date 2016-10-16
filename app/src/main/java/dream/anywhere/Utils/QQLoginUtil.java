package dream.anywhere.Utils;

/**
 * Created by SKYMAC on 16/9/11.
 */
import android.app.Activity;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * QQ登录的工具类
 */
public class QQLoginUtil {
    // 应用在腾讯官方所对应的APP_ID
    private String APP_ID = "222222";
    // 声明控件
    private Tencent mTencent;
    private Activity activity;
    private QQLoginListener loginListener;

    public QQLoginUtil(Activity activity) {
        this.activity = activity;
        mTencent = Tencent.createInstance(APP_ID, activity);
    }

    public void login(QQLoginListener loginListener) {
        this.loginListener = loginListener;
        mTencent.login(activity, "all", new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject values) {

                initOpenidAndToken(values);
                updateUserInfo();
            }
        });
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    loginListener.loginError("登录认证失败");
                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    try {
                        loginListener.loginSuccess(mTencent.getOpenId(),
                                json.has("nickname") ? json.getString("nickname") : null,
                                json.has("figureurl_qq_2") ? json.getString("figureurl_qq_2") : null,
                                json.has("gender") ? json.getString("gender") : null);
                    } catch (Exception e) {
                        loginListener.loginError("返回的数据有误");
                    }
                }

                @Override
                public void onCancel() {
                    loginListener.loginCancel();
                }
            };
            UserInfo mInfo = new UserInfo(activity, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
            loginListener.loginError("登录认证失败");
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                loginListener.loginError("登录认证失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                loginListener.loginError("返回的数据有误");
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onError(UiError e) {
            loginListener.loginError(e.errorDetail);
        }

        @Override
        public void onCancel() {
            loginListener.loginCancel();
        }
    }

    public interface QQLoginListener {
        public void loginSuccess(String uuid, String nickname, String photo, String gender);

        public void loginError(String errorMessage);

        public void loginCancel();
    }
}