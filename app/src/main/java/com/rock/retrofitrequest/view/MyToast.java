package com.rock.retrofitrequest.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rock.retrofitrequest.R;


/**
 * Created by Administrator on 2018/2/6/006.
 * 自定义弹出框
 */

public class MyToast {

    private final Toast mToast;

    private MyToast(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);
        TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
        toast_tv.setText(text);
        if (text.length() > 13) {
            FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(28, 28, 28, 28);
            toast_tv.setLayoutParams(lp2);
        } else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toast_tv.getLayoutParams();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 57, context.getResources().getDisplayMetrics());
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 192, context.getResources().getDisplayMetrics());
            toast_tv.setGravity(Gravity.CENTER);
            toast_tv.setLayoutParams(layoutParams);
        }
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(view);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    /**
     * 弹出框位置
     *
     * @param gravity Gravity.CENTER
     *                Gravity.LEFT
     *                Gravity.RIGHT
     *                Gravity.TOP
     *                Gravity.BOTTOM
     * @param xOffset 偏移量
     * @param yOffset
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
