//package dream.anywhere.Utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.view.View;
//
//import java.io.File;
//import java.util.Timer;
//
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.DownloadFileListener;
//import dream.anywhere.Activity.HomeActivity;
//import dream.anywhere.Adapter.HomeViewPagerAdapter;
//import dream.anywhere.Application.MyApplication;
//
///**
// * Created by SKYMAC on 16/9/1.
// * <p/>
// * 每一个需要刷新的视图 都在这个方法里
// */
//public class LoginFlushViewUtils {
//    public static void flushView() {
////        HomeActivity.homeAct.runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//        Log.i("Act", " ========= flushView ======== ");
//        if (MyApplication.user != null) {
//            //如果任何一个控件没有加载完毕 则递归调用自己 重新尝试
//            if (HomeActivity.relativeOrderBefore == null || HomeActivity.relativeOrderAfter == null || HomeActivity.mineUserRelative == null || HomeActivity.mineUserNameTv == null || HomeActivity.userIconImg == null) {
////                    new Thread(new Runnable() {
////                        @Override
////                        public void run() {
////                            try {
////                                Thread.sleep(1000);
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
////                            flushView();
////                        }
////                    }).start();
//
////                flushView();
//            } else {
//                Log.i("Act", " ========= loginflushfinish ======== ");
//                HomeActivity.relativeOrderBefore.setVisibility(View.INVISIBLE);
//                HomeActivity.relativeOrderAfter.setVisibility(View.VISIBLE);
//                if (MyApplication.user.getNickName() == null) {
//                    HomeActivity.mineUserNameTv.setText("未设置昵称");
//                } else {
//                    HomeActivity.mineUserNameTv.setText(MyApplication.user.getNickName());
//                }
//                HomeActivity.mineUserRelative.setClickable(false);
//                HomeActivity.userIconImg.setClickable(true);
//
//                //二级缓存头像
//                final File file = new File(MyApplication.file, "/" + MyApplication.user.getObjectId() + "/" + MyApplication.user.getObjectId() + ".jpeg");
//                Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());
//                if (bit == null) {
//                    //如果用户设置了头像的话
//                    if (MyApplication.user.getUserIcon() != null) {
//                        MyApplication.user.getUserIcon().download(file.getAbsoluteFile(), new DownloadFileListener() {
//                            @Override
//                            public void done(String s, BmobException e) {
//                                if (e == null) {
//                                    //bit设置不了final
//                                    final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                                    Log.i("MyFile", "======BitmapFactory.decodeFile(file.getAbsolutePath():=====" + file.getAbsoluteFile());
//                                    //第二步 设置图片
//                                    HomeActivity.userIconImg.setImageBitmap(bitmap);
//                                    //第三部 缓存到内存中
////                    cache.put(str32, bitmap);
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(Integer integer, long l) {
//
//                            }
//                        });
//                    }
//                } else {
//                    HomeActivity.userIconImg.setImageBitmap(bit);
//                }
//
//
//                HomeActivity.vp.getAdapter().notifyDataSetChanged();
//            }
//
//
//            //如果主页正在刷新数据 关闭提示框
////                if (HomeActivity.progressDialog.isShowing()) {
////                    HomeActivity.progressDialog.dismiss();
////                }
//
//        } else {
//            //未登陆状态下的刷新
//            if (HomeActivity.relativeOrderBefore == null || HomeActivity.relativeOrderAfter == null || HomeActivity.mineUserRelative == null || HomeActivity.mineUserNameTv == null || HomeActivity.userIconImg == null) {
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        try {
////                            Thread.sleep(1000);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                        flushView();
////                    }
////                }).start();
//
////                flushView();
//            } else {
//                Log.i("Act", " ========= unloginflushfinish ======== ");
//                HomeActivity.relativeOrderBefore.setVisibility(View.VISIBLE);
//                HomeActivity.relativeOrderAfter.setVisibility(View.INVISIBLE);
//                HomeActivity.mineUserNameTv.setText("登陆/注册");
//                HomeActivity.mineUserRelative.setClickable(true);
//                HomeActivity.userIconImg.setClickable(false);
//
//                //如果主页正在刷新数据 关闭提示框
////                if (HomeActivity.progressDialog.isShowing()) {
////                    HomeActivity.progressDialog.dismiss();
////                }
//                HomeActivity.vp.getAdapter().notifyDataSetChanged();
//            }
//        }
//    }
////        });
////    }
//}
