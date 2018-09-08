package com.rock.retrofitrequest.https.download;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rock.retrofitrequest.R;


/**
 * 升级弹出框
 */

public class DownloadDialog extends Dialog {
    private static Context mContext;
    private TextView down_tv;
    private ProgressBar down_mProgressBar;
    private View view;
    private WindowManager windowManager;
    private static DownloadDialog dialog;

    public DownloadDialog(Context context) {
        super(context);
        windowManager = ((Activity) context).getWindowManager();
    }

    public static DownloadDialog getInstance(Activity context) {
        mContext = context;
        if (dialog == null) {
            dialog = new DownloadDialog(context);
        }
        dialog.show();
        return dialog;
    }

    public static void dimissDialog() {
        dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除屏幕title
        //设置对话框样式
        setStyle();
        //初始化控件
        initView();
    }

    private void initView() {
        view = View.inflate(mContext, R.layout.dwonlaod_dialog, null);
        setContentView(view);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(Gravity.CENTER);
        Point display = new Point();
        windowManager.getDefaultDisplay().getSize(display); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (display.x * 0.8);
        onWindowAttributesChanged(p);
        getWindow().setAttributes(p);
        down_tv = (TextView) view.findViewById(R.id.down_tv);
        down_mProgressBar = (ProgressBar) view.findViewById(R.id.down_mProgressBar);
    }

    private void setStyle() {
        //设置对话框不可取消
        this.setCancelable(false);
        //设置触摸对话框外面不可取消
        this.setCanceledOnTouchOutside(false);
    }

    //设置进度条
    public void setProgress(int progress) {
        down_tv.setText("升级中" + progress + "%");
        down_mProgressBar.setProgress(progress);
    }
}
