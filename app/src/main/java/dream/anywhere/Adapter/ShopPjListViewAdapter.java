package dream.anywhere.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Bean.OrderPjBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/10.
 */
public class ShopPjListViewAdapter extends BaseAdapter {

    private List<OrderPjBean> orderPjBeanList;
    private Context context;
    private LayoutInflater inflater;

    public ShopPjListViewAdapter(List<OrderPjBean> orderPjBeanList, Context context) {
        this.orderPjBeanList = orderPjBeanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderPjBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderPjBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_shop_pj, null);
            vh.userNameTv = (TextView) view.findViewById(R.id.item_shop_pj_userTv);
            vh.textTv = (TextView) view.findViewById(R.id.item_shop_pj_textTv);
            vh.timeTv = (TextView) view.findViewById(R.id.item_shop_pj_timeTv);
            vh.ratingBar = (RatingBar) view.findViewById(R.id.item_shop_pj_ratingbar);
            vh.img = (ImageView) view.findViewById(R.id.item_shop_pj_img);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        OrderPjBean pj = orderPjBeanList.get(position);

        BmobQuery<UserBean> query = new BmobQuery<UserBean>();
        query.addWhereEqualTo("objectId", pj.getUser().getObjectId());
        final ViewHolder finalVh = vh;
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                finalVh.userNameTv.setText(list.get(0).getNickName());
            }
        });


        vh.textTv.setText(pj.getText());
        vh.timeTv.setText(pj.getCreatedAt());
        vh.ratingBar.setRating(pj.getStar());
        if (pj.getGood()) {
            vh.img.setVisibility(View.VISIBLE);
        }
        return view;
    }

    class ViewHolder {
        TextView userNameTv, textTv, timeTv;
        RatingBar ratingBar;
        ImageView img;
    }
}
