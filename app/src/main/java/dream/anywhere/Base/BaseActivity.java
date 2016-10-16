package dream.anywhere.Base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SKYMAC on 16/8/29.
 */
public class BaseActivity extends FragmentActivity {

    public Activity act;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        act = null;
    }

    public TextView findTv(int id) {
        return (TextView) findViewById(id);
    }

    public EditText findEt(int id) {
        return (EditText) findViewById(id);
    }

    public Button findBut(int id) {
        return (Button) findViewById(id);
    }

    public ImageView findImg(int id) {
        return (ImageView) findViewById(id);
    }

    public ListView findLv(int id) {
        return (ListView) findViewById(id);
    }

    public GridView findGv(int id) {
        return (GridView) findViewById(id);
    }

    public LinearLayout findLin(int id) {
        return (LinearLayout) findViewById(id);
    }

    public RelativeLayout findRel(int id) {
        return (RelativeLayout) findViewById(id);
    }

    public void toastS(String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    public void toastL(String text) {
        Toast.makeText(act, text, Toast.LENGTH_LONG).show();
    }

    public void logI(String text) {
        Log.i("Act", text);
    }

    public void logE(String text) {
        Log.e("Act", text);
    }

    public void showProgressDialog(String title, String message, boolean isCancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        progressDialog.show();
    }

    public void dismissProgressdialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void startAct(Class<?> cls) {
        startActivity(new Intent(act, cls));
    }

    public ProgressDialog createProgressDialog(String title, String message, boolean flag) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(flag);
//        progressDialog.show();
        return progressDialog;
    }
}
