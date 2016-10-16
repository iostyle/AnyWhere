package dream.anywhere.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import dream.anywhere.R;
import dream.anywhere.Utils.QQLoginUtil;

/**
 * 二次登录
 *
 * @author 么么哒
 */
@SuppressLint("ShowToast")
public class SecondActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qqlogin);
		// 从当前加载的布局文件转换的视图中 获取Button的引用并设置监听事件
		findViewById(R.id.new_login_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		new QQLoginUtil(this).login(new QQLoginUtil.QQLoginListener() {
			@Override
			public void loginSuccess(String uuid, String nickname, String photo, String gender) {
				// GBApplication
				// .Instance()
				// .doAction(
				// com.cliff.account.controller.ActionCode.THIRD_LOGIN,
				// 1, uuid, nickname, photo, gender);
				((TextView) SecondActivity.this.findViewById(R.id.user_nickname))
						.setText(nickname + "   " + photo);
			}

			@Override
			public void loginError(String errorMessage) {
				// UIUtil.showMessageText(LoginActivity.this, errorMessage);
				Toast.makeText(SecondActivity.this, "异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void loginCancel() {
				// UIUtil.showMessageText(LoginActivity.this, "用户已取消");
				Toast.makeText(SecondActivity.this, "用户取消", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
