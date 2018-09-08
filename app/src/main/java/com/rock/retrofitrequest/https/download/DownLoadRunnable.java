package com.rock.retrofitrequest.https.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;

import com.orhanobut.hawk.Hawk;
import com.rock.retrofitrequest.app.MyApp;
import com.rock.retrofitrequest.utils.Constant;


/**
 * 下载更新类
 */

public class DownLoadRunnable implements Runnable {
    private String url;
    private Handler handler;
    private Context mContext;

    public DownLoadRunnable(Context context, String url, Handler handler) {
        this.mContext = context;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        //设置线程优先级为后台，这样当多个线程并发后很多无关紧要的线程分配的CPU时间将会减少，有利于主线程的处理
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        //具体下载方法
        if (!canDownloadState()) {
            MyApp.T(mContext, "您已禁用下载管理服务，请开启下载服务");
            updateCanDownloadState();
            return;
        }
        startDownload();
    }

    //判断是否可以启动下载服务
    private boolean canDownloadState() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void updateCanDownloadState() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContext.startActivity(intent);
    }

    private long startDownload() {
        //获得DownloadManager对象
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //获得下载id，这是下载任务生成时的唯一id，可通过此id获得下载信息
        long requestId = downloadManager.enqueue(CreateRequest(url));
        Hawk.put(Constant.DOWN_ID, requestId);
        //查询下载信息方法
        queryDownloadProgress(requestId, downloadManager);
        return requestId;
    }

    private void queryDownloadProgress(long requestId, DownloadManager downloadManager) {
        DownloadManager.Query query = new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging = true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {

                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DownloadManager.Request CreateRequest(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);// 隐藏notification
        request.setAllowedNetworkTypes(request.NETWORK_WIFI);//设置下载网络环境为wifi
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "HuoLaiLe.apk");//指定apk缓存路径，默认是在SD卡中的Download文件夹
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String sdPath = Environment.getExternalStorageDirectory() + "/";
//            String mSavePath = sdPath + "HuoLaiLe/app";
//            File dir = new File(mSavePath);
//            if (!dir.exists()) {
//                dir.mkdir();
//                request.setDestinationInExternalPublicDir(mSavePath, "HuoLaiLe.apk");
//            } else {
//                request.setDestinationInExternalPublicDir(mSavePath, "HuoLaiLe.apk");
//            }
//            request.setMimeType("application/vnd.android.package-archive");
//        } else {
//            WCApplication.T(mContext, "没有sd卡");
//        }
        return request;
    }


//    @Override
//    public void onDownDialogDismiss(DownloadDialog dialog, View view) {
//        switch (view.getId()) {
//            case R.id.down_cancel_ll:
//                downloadManager.remove(requestId);
//                dialog.dismiss();
//                break;
//        }
//    }

}
