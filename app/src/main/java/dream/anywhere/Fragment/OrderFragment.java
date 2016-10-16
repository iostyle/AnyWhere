package dream.anywhere.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import dream.anywhere.Activity.LoginActivity;
import dream.anywhere.Base.BaseFragment;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/8/30.
 */
public class OrderFragment extends BaseFragment implements BaseInterface {

    @Override
    protected void init() {
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_order,null);
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


}
