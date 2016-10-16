package dream.anywhere.Activity;

import android.os.Bundle;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import dream.anywhere.Adapter.SetOrderGiftListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/11.
 */
public class SelectGiftActivity extends BaseActivity implements BaseInterface {

    public static SelectGiftActivity activity;
    @ViewInject(R.id.act_select_gift_listview)
    private ListView listView;
    private SetOrderGiftListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_gift);
        activity = this;
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
        adapter = new SetOrderGiftListViewAdapter(MyApplication.user.getGiftBeanList(), act);

    }

    @Override
    public void InitViewOper() {
        listView.setAdapter(adapter);
    }
}
