package com.rock.retrofitrequest.https.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;
import com.rock.retrofitrequest.utils.Constant;

import java.io.File;

/**
 * 安装app
 */

public class InstallApkBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long cacheDownLoadId = Hawk.get(Constant.DOWN_ID);
        long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);//139
        if (cacheDownLoadId == downLoadId) {
            File apkFile = queryDownloadedApk(context);
            Intent intents = new Intent(Intent.ACTION_VIEW);
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intents.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.wnhz.workscoming.fileprovider", apkFile);
                intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intents.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intents.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intents);

        }
    }

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    public static File queryDownloadedApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = Hawk.get(Constant.DOWN_ID);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }
}

