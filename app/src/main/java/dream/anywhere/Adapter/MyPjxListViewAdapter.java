package dream.anywhere.Adapter;

import android.content.Context;
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
import cn.bmob.v3.listener.QueryListener;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.OrderPjBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/10.
 */
public class MyPjxListViewAdapter extends BaseAdapter {

    private List<OrderPjBean> orderPjBeanList;
    private Context context;
    private LayoutInflater inflater;

    public MyPjxListViewAdapter(List<OrderPjBean> orderPjBeanList, Context context) {
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
            view = inflater.inflate(R.layout.item_my_pj, null);
            vh.goodImg = (ImageView) view.findViewById(R.id.item_my_pj_goodImg);
            vh.shopNameTv = (TextView) view.findViewById(R.id.item_my_pj_shopNameTv);
            vh.orderNumTv = (TextView) view.findViewById(R.id.item_my_pj_orderNumTv);
            vh.textTv = (TextView) view.findViewById(R.id.item_my_pj_textTv);
            vh.timeTv = (TextView) view.findViewById(R.id.item_my_pj_timeTv);
            vh.ratingBar = (RatingBar) view.findViewById(R.id.item_my_pj_ratingBar);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        OrderPjBean pj = orderPjBeanList.get(position);

        BmobQuery<ShopBean> query = new BmobQuery<ShopBean>();
        final ViewHolder finalVh = vh;
        query.getObject(pj.getShopObjectId(), new QueryListener<ShopBean>() {

            @Override
            public void done(ShopBean object, BmobException e) {
                if (e == null) {
                    finalVh.shopNameTv.setText(object.getName());
                } else {
                }
            }

        });

        vh.orderNumTv.setText(pj.getOrderNum());
        vh.textTv.setText(pj.getText());
        vh.timeTv.setText(pj.getCreatedAt());
        vh.ratingBar.setRating(pj.getStar());
        if (pj.getGood()) {
            vh.goodImg.setVisibility(View.VISIBLE);
        }


        return view;
    }

    class ViewHolder {
        ImageView goodImg;
        TextView shopNameTv, orderNumTv, textTv, timeTv;
        RatingBar ratingBar;
    }
}
