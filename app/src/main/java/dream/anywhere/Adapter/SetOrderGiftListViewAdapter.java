package dream.anywhere.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dream.anywhere.Activity.SelectGiftActivity;
import dream.anywhere.Activity.SetOrderActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Bean.GiftBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/11.
 */
public class SetOrderGiftListViewAdapter extends BaseAdapter {

    private List<GiftBean> giftBeanList;
    private Context context;
    private LayoutInflater inflater;

    public SetOrderGiftListViewAdapter(List<GiftBean> giftBeanList, Context context) {
        this.giftBeanList = giftBeanList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return giftBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return giftBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_gift_listview, null);
        final GiftBean gift = giftBeanList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.item_gift_tv);
        tv.setText(gift.getMoney() + "");
        ImageView img = (ImageView) view.findViewById(R.id.item_gift_img);
        if (gift.isUsed()) {
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.INVISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApplication.putData("SetOrderGift", gift);
                    MyApplication.putData("SetOrderGiftPosition", position);
                    SetOrderActivity.giftLin.setClickable(false);
                    SelectGiftActivity.activity.finish();
                }
            });
        }
        return view;
    }
}
