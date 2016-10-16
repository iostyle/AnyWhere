package dream.anywhere.Utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dream.anywhere.Bean.ShopBean;

/**
 * Created by SKYMAC on 16/9/4.
 */
public class ShopUtils {
    /**
     * type 为0 查询所有 data null
     * type 为1 查询类别 data 类别
     * type 为2 加载更多 data 忽略条数
     */

    public static void findShop(int limit, int type, Object data, final FindListener<ShopBean> listener) {
        BmobQuery<ShopBean> query = new BmobQuery<ShopBean>();
        query.setLimit(limit);
        //添加查询条件
        switch (type) {
            case 0: {
                query.order("-createdAt");
                query.findObjects(listener);
            }
            break;
            case 1: {
                query.order("-createdAt");
                query.addWhereContains("type", data.toString());
//                query.findObjects(new FindListener<ShopBean>() {
//                    @Override
//                    public void done(List<ShopBean> list, BmobException e) {
//                        listener.done(list, e);
//                    }
//                });
                query.findObjects(listener);
            }
            break;
//            case 2:{
//                query.order("-createdAt");
//                query.addWhereContains("type", data.toString());
//                query.setSkip((Integer) data);
//                //执行查询方法
//                query.findObjects(listener);
//                break;
//            }
        }
//        query.findObjects(new FindListener<ShopBean>() {
//            @Override
//            public void done(List<ShopBean> list, BmobException e) {
//                listener.done(list,e);
//            }
//        });
    }
}