package dream.anywhere.Base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by SKYMAC on 16/8/30.
 */
public abstract class BaseFragment extends Fragment {
    public Activity act;
    private View view;
    private boolean flag = true;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = activity;
    }

    /**
     * 下面两个方法是优化
     */
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //final禁止重写
        if (view == null) {
            view = initContentView(inflater, container);
        }
        return view;
    }


    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (flag) {
            init();
            flag = false;
        }
    }

    protected abstract void init();

    protected abstract View initContentView(LayoutInflater inflater, ViewGroup container);

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
            progressDialog = new ProgressDialog(act);
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

}
