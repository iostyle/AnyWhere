package dream.anywhere.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Activity.AddressActivity;
import dream.anywhere.Activity.AddressAddActivity;
import dream.anywhere.Activity.SelectAddressActivity;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Bean.AddressBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/7.
 */
public class AddressListViewAdapter extends BaseAdapter {

    private List<AddressBean> addressBeanList;
    private Context context;
    private LayoutInflater inflater;

    //为true代表可点击 false不可点击
    private boolean flag;

    public AddressListViewAdapter(List<AddressBean> addressBeanList, Context context, boolean flag) {
        this.addressBeanList = addressBeanList;
        this.context = context;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return addressBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return addressBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        if (view == null) {
            view = inflater.inflate(R.layout.item_address_listview, null);
            vh.nameTv = (TextView) view.findViewById(R.id.item_address_nameTv);
            vh.sexTv = (TextView) view.findViewById(R.id.item_address_sexTv);
            vh.phoneTv = (TextView) view.findViewById(R.id.item_address_phoneTv);
            vh.addressTv = (TextView) view.findViewById(R.id.item_address_addressTv);
            vh.edit = (ImageView) view.findViewById(R.id.item_address_editImg);
            vh.delete = (ImageView) view.findViewById(R.id.item_address_deleteImg);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final AddressBean address = addressBeanList.get(position);
        vh.nameTv.setText(address.getName());
        vh.sexTv.setText(address.getSex());
        vh.phoneTv.setText(address.getPhone());
        String site = address.getSite();
        if (site == null) {
            site = "";
        }
        vh.addressTv.setText(address.getAddress() + " " + site);
        vh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.putData("EditAddress", address);
                context.startActivity(new Intent(context, AddressAddActivity.class));
            }
        });
        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("提示").setMessage("您确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyApplication.user.getAddressBeanList().remove(position);
                        UserBean u = new UserBean();
                        u.setAddressBeanList(MyApplication.user.getAddressBeanList());
                        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                    MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                                    AddressActivity.addressListViewAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });
        //可点击
        if(flag){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApplication.putData("selectAddress",address);
                    SelectAddressActivity.activity.finish();
                }
            });
        }
        return view;
    }

    class ViewHolder {
        TextView nameTv, sexTv, phoneTv, addressTv;
        ImageView edit, delete;
    }
}
