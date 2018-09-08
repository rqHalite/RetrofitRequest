package com.rock.retrofitrequest.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.rock.retrofitrequest.view.MyToast;

/**
 * Created by Rock on 2018/5/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static void T(Context context, String msg) {
//        LToastUtil.T(context,msg, ContextCompat.getColor(context,R.color.snackBarBackground),R.drawable.ic_huo);
        MyToast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void T(String msg) {
        T(this, msg);
    }
}
