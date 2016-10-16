package dream.anywhere.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.anywhere.Adapter.SetOrderListViewAdapter;
import dream.anywhere.Application.MyApplication;
import dream.anywhere.Base.BaseActivity;
import dream.anywhere.Base.BaseInterface;
import dream.anywhere.Bean.AddressBean;
import dream.anywhere.Bean.BuyFoodBean;
import dream.anywhere.Bean.OrderBean;
import dream.anywhere.Bean.ShopBean;
import dream.anywhere.Bean.UserBean;
import dream.anywhere.R;

/**
 * Created by SKYMAC on 16/9/7.
 */
public class OrderActivity extends BaseActivity implements BaseInterface {

    private OrderBean order;

    @ViewInject(R.id.act_Order_address_name)
    private TextView addressName;
    @ViewInject(R.id.act_Order_address_sex)
    private TextView addressSex;
    @ViewInject(R.id.act_Order_address_phone)
    private TextView addressPhone;
    @ViewInject(R.id.act_Order_address_address)
    private TextView addressAddress;

    private AddressBean address;

    @ViewInject(R.id.act_order_foodListView)
    private ListView listView;
    private SetOrderListViewAdapter adapter;

    private List<BuyFoodBean> buyFoodBeanList;
    @ViewInject(R.id.act_order_paymoneyTv)
    private TextView payMoneyTv;
    @ViewInject(R.id.act_order_moneyLin)
    private LinearLayout payMoneyLin;
    @ViewInject(R.id.act_order_submitTv)
    private TextView submitTv;

    private ShopBean shop;

    @ViewInject(R.id.act_order_sendTime)
    private TextView sendTimeTv;
    //    @ViewInject(R.id.act_order_sendLin)
//    private LinearLayout sendLin;
    @ViewInject(R.id.act_order_shopNameTv)
    private TextView shopNameTv;

    //选择支付方式
    @ViewInject(R.id.act_order_spinner)
    private Spinner spinner;

    //记录支付方式
    private String payType = "钱包余额";

    private ProgressDialog dialog;

    //订单号码
    @ViewInject(R.id.act_order_orderNumTv)
    private TextView orderNumTv;

    //评价按钮
    @ViewInject(R.id.act_order_PjTv)
    private TextView orderPjTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order);
        InitView();
        InitData();
        InitViewOper();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!order.getPay()) {
            payMoneyLin.setVisibility(View.VISIBLE);
            submitTv.setText("支付");

            submitTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (payType.equals("钱包余额")) {
                        if (order.getPayPrice() > MyApplication.user.getMoney()) {
                            toastL("余额不足，请您先充值！");
                            return;
                        }
                        UserBean u = new UserBean();
                        u.setMoney(MyApplication.user.getMoney() - order.getPayPrice());
                        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    OrderBean o = new OrderBean();
                                    o.setPay(true);
                                    o.update(order.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            toastS("付款成功!");
                                            payMoneyLin.setVisibility(View.INVISIBLE);
//                                        sendLin.setVisibility(View.VISIBLE);
//                                        sendTimeTv.setText(shop.getPeisongshijian());
                                            MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                                            submitTv.setText("确认收货");
//                                        submitTv.setText(order.getShop().getPeisongshijian().toString());
                                            submitTv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    OrderBean o = new OrderBean();
                                                    o.setReceive(true);
                                                    o.update(order.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            toastS("订单完成！");
//                                                        sendLin.setVisibility(View.INVISIBLE);
                                                            submitTv.setText("订单完成");
                                                            submitTv.setClickable(false);

                                                            //刷新Order
                                                            BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
                                                            query.getObject(order.getObjectId(), new QueryListener<OrderBean>() {
                                                                @Override
                                                                public void done(OrderBean orderBean, BmobException e) {
                                                                    order = orderBean;

                                                                    if (!order.getPj()) {
                                                                        orderPjTv.setVisibility(View.VISIBLE);
                                                                        orderPjTv.setClickable(true);
                                                                        orderPjTv.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                MyApplication.putData("PjOrder",order);
                                                                                startAct(OrderPjActivity.class);
                                                                            }
                                                                        });
                                                                    } else {
                                                                        orderPjTv.setClickable(false);
                                                                        orderPjTv.setVisibility(View.INVISIBLE);
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    } else if (payType.equals("支付宝")) {
                        pay(true);
                    } else if (payType.equals("微信支付")) {
                        pay(false);
                    } else {
                        toastL("请选择支付方式");
                    }
                }
            });
        } else {
            payMoneyLin.setVisibility(View.INVISIBLE);
            if (!order.getReceive()) {
//                sendTimeTv.setText(shop.getPeisongshijian());
//                sendLin.setVisibility(View.VISIBLE);
                submitTv.setText("确认收货");
                submitTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderBean o = new OrderBean();
                        o.setReceive(true);
                        o.update(order.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                toastS("订单完成！");
//                                sendLin.setVisibility(View.INVISIBLE);
//                                startAct(OrderActivity.class);
//                                finish();
                                submitTv.setText("订单完成");
                                submitTv.setClickable(false);

                                //刷新Order
                                BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
                                query.getObject(order.getObjectId(), new QueryListener<OrderBean>() {
                                    @Override
                                    public void done(OrderBean orderBean, BmobException e) {
                                        order = orderBean;

                                        if (!order.getPj()) {
                                            orderPjTv.setVisibility(View.VISIBLE);
                                            orderPjTv.setClickable(true);
                                            orderPjTv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    MyApplication.putData("PjOrder",order);
                                                    startAct(OrderPjActivity.class);
                                                }
                                            });
                                        } else {
                                            orderPjTv.setClickable(false);
                                            orderPjTv.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });


                            }
                        });
                    }
                });
            } else {
                submitTv.setText("订单完成");
                submitTv.setClickable(false);

                if (!order.getPj()) {
                    orderPjTv.setVisibility(View.VISIBLE);
                    orderPjTv.setClickable(true);
                    orderPjTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyApplication.putData("PjOrder",order);
                            startAct(OrderPjActivity.class);
                        }
                    });
                } else {
                    orderPjTv.setClickable(false);
                    orderPjTv.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void InitView() {
        ViewUtils.inject(act);
    }

    @Override
    public void InitData() {
        order = (OrderBean) MyApplication.getData("ClickOrder", true);
        String shopObjectId = order.getShopObjectId();
//        BmobQuery<ShopBean> query = new BmobQuery<>();
//        query.addWhereEqualTo("objectid", shopObjectId);
//        query.findObjects(new FindListener<ShopBean>() {
//            @Override
//            public void done(List<ShopBean> list, BmobException e) {
//                if (e == null) {
//                    shop = list.get(0);
//                    logE("OrderAct:" + shop.toString());
//                }
//            }
//        });
        BmobQuery<ShopBean> bmobQuery = new BmobQuery<ShopBean>();
        bmobQuery.getObject(shopObjectId, new QueryListener<ShopBean>() {
            @Override
            public void done(ShopBean object, BmobException e) {
                if (e == null) {
                    shop = object;
                    logE("OrderAct:" + shop.toString());
                    shopNameTv.setText(shop.getName());
                    sendTimeTv.setText(shop.getPeisongshijian() + "");
                } else {
                }
            }
        });

        address = order.getAddress();
        buyFoodBeanList = order.getBuyFoodBeanList();
        adapter = new SetOrderListViewAdapter(buyFoodBeanList, act);
        logE("OrderAct:" + order.toString());

        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listView.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);


    }

    @Override
    public void InitViewOper() {
        addressName.setText(address.getName());
        addressSex.setText(address.getSex());
        addressPhone.setText(address.getPhone());
        addressAddress.setText(address.getAddress() + " " + address.getSite());
        listView.setAdapter(adapter);
        payMoneyTv.setText(order.getPayPrice().toString());
        orderNumTv.setText(order.getOrderNum());

        if (!order.getPay()) {
            payMoneyLin.setVisibility(View.VISIBLE);
            submitTv.setText("支付");

            submitTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (payType.equals("钱包余额")) {
                        if (order.getPayPrice() > MyApplication.user.getMoney()) {
                            toastL("余额不足，请您先充值！");
                            return;
                        }
                        UserBean u = new UserBean();
                        u.setMoney(MyApplication.user.getMoney() - order.getPayPrice());
                        u.update(MyApplication.user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    OrderBean o = new OrderBean();
                                    o.setPay(true);
                                    o.update(order.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            toastS("付款成功!");
                                            payMoneyLin.setVisibility(View.INVISIBLE);
//                                        sendLin.setVisibility(View.VISIBLE);
//                                        sendTimeTv.setText(shop.getPeisongshijian());
                                            MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                                            submitTv.setText("确认收货");
//                                        submitTv.setText(order.getShop().getPeisongshijian().toString());
                                            submitTv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    OrderBean o = new OrderBean();
                                                    o.setReceive(true);
                                                    o.update(order.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            toastS("订单完成！");
//                                                        sendLin.setVisibility(View.INVISIBLE);
                                                            submitTv.setText("订单完成");
                                                            submitTv.setClickable(false);

                                                            //刷新Order
                                                            BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
                                                            query.getObject(order.getObjectId(), new QueryListener<OrderBean>() {
                                                                @Override
                                                                public void done(OrderBean orderBean, BmobException e) {
                                                                    order = orderBean;

                                                                    if (!order.getPj()) {
                                                                        orderPjTv.setVisibility(View.VISIBLE);
                                                                        orderPjTv.setClickable(true);
                                                                        orderPjTv.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                MyApplication.putData("PjOrder",order);
                                                                                startAct(OrderPjActivity.class);
                                                                            }
                                                                        });
                                                                    } else {
                                                                        orderPjTv.setClickable(false);
                                                                        orderPjTv.setVisibility(View.INVISIBLE);
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    } else if (payType.equals("支付宝")) {
                        pay(true);
                    } else if (payType.equals("微信支付")) {
                        pay(false);
                    } else {
                        toastL("请选择支付方式");
                    }
                }
            });
        } else {
            payMoneyLin.setVisibility(View.INVISIBLE);
            if (!order.getReceive()) {
//                sendTimeTv.setText(shop.getPeisongshijian());
//                sendLin.setVisibility(View.VISIBLE);
                submitTv.setText("确认收货");
                submitTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderBean o = new OrderBean();
                        o.setReceive(true);
                        o.update(order.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                toastS("订单完成！");
//                                sendLin.setVisibility(View.INVISIBLE);
//                                startAct(OrderActivity.class);
//                                finish();
                                submitTv.setText("订单完成");
                                submitTv.setClickable(false);

                                //刷新Order
                                BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
                                query.getObject(order.getObjectId(), new QueryListener<OrderBean>() {
                                    @Override
                                    public void done(OrderBean orderBean, BmobException e) {
                                        order = orderBean;

                                        if (!order.getPj()) {
                                            orderPjTv.setVisibility(View.VISIBLE);
                                            orderPjTv.setClickable(true);
                                            orderPjTv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    MyApplication.putData("PjOrder",order);
                                                    startAct(OrderPjActivity.class);
                                                }
                                            });
                                        } else {
                                            orderPjTv.setClickable(false);
                                            orderPjTv.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });


                            }
                        });
                    }
                });
            } else {
                submitTv.setText("订单完成");
                submitTv.setClickable(false);

                if (!order.getPj()) {
                    orderPjTv.setVisibility(View.VISIBLE);
                    orderPjTv.setClickable(true);
                    orderPjTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyApplication.putData("PjOrder",order);
                            startAct(OrderPjActivity.class);
                        }
                    });
                } else {
                    orderPjTv.setClickable(false);
                    orderPjTv.setVisibility(View.INVISIBLE);
                }
            }
        }

        //支付方式
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                          {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  String[] pay = getResources().getStringArray(R.array.pay);
//                Toast.makeText(act, "你点击的是:"+pay[i], Toast.LENGTH_LONG).show();
                                                  payType = pay[i];
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {

                                              }
                                          }

        );
    }

    @OnClick(R.id.act_order_backLinearLayout)
    public void onBackClick(View v) {
        finish();
    }


    //支付相关

    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在获取订单...");
        final String name = "美团外卖订单" + order.getOrderNum();

        BP.pay(name, "美团外卖订单" + order.getOrderNum(), Double.parseDouble(payMoneyTv.getText().toString()), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(act, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
//                UserBean u = new UserBean();
//                u.setMoney(MyApplication.user.getMoney() + getPrice());
//                u.update(MyApplication.user.getObjectId(), new UpdateListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        MyApplication.user= BmobUser.getCurrentUser(UserBean.class);
//                        Toast.makeText(act, "支付成功!", Toast.LENGTH_SHORT).show();
//                        hideDialog();
//                        finish();
//                    }
//                });
                OrderBean o = new OrderBean();
                o.setPay(true);
                o.update(order.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        hideDialog();
                        toastS("付款成功!");
                        payMoneyLin.setVisibility(View.INVISIBLE);
//                                        sendLin.setVisibility(View.VISIBLE);
//                                        sendTimeTv.setText(shop.getPeisongshijian());
                        MyApplication.user = BmobUser.getCurrentUser(UserBean.class);
                        submitTv.setText("确认收货");
//                                        submitTv.setText(order.getShop().getPeisongshijian().toString());
                        submitTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderBean o = new OrderBean();
                                o.setReceive(true);
                                o.update(order.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        toastS("订单完成！");
//                                                        sendLin.setVisibility(View.INVISIBLE);
                                        submitTv.setText("订单完成");
                                        submitTv.setClickable(false);

                                        //刷新Order
                                        BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
                                        query.getObject(order.getObjectId(), new QueryListener<OrderBean>() {
                                            @Override
                                            public void done(OrderBean orderBean, BmobException e) {
                                                order = orderBean;

                                                if (!order.getPj()) {
                                                    orderPjTv.setVisibility(View.VISIBLE);
                                                    orderPjTv.setClickable(true);
                                                    orderPjTv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            MyApplication.putData("PjOrder",order);
                                                            startAct(OrderPjActivity.class);
                                                        }
                                                    });
                                                } else {
                                                    orderPjTv.setClickable(false);
                                                    orderPjTv.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }
                });

            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            act,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(act, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                hideDialog();
            }
        });
    }


    /////////////////

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
