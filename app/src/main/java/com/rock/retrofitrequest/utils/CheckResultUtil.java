package com.rock.retrofitrequest.utils;

import android.content.Context;
import android.content.Intent;

import com.rock.retrofitrequest.MainActivity;

/**
 * Created by Rock on 2018/5/22.
 */

public class CheckResultUtil {

    public static void onLoginFailure(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        logout(context);
        context.startActivity(intent);//回到主页
    }

    public static void logout(Context context){
//        /**过时数据**/
////        MyApplication.loginUser = null;//上下文用户信息清除
////        MyApplication.isLogin = false;//登陆状态改变
////        MyApplication.getInstance().phone = "";//清除账号
////        MyApplication.getInstance().passWord = "";//清除缓存密码
//        /**新数据**/
//        Settings.putRongToken(context,"");//清除聊天ID
//        Settings.putUserToken(context,"");//清除用户ID
//        RongCloudUtil.disconnect();//断开聊天连接
//        if(!JPushInterface.isPushStopped(context.getApplicationContext()))//检查是否推送已停止
//            JPushInterface.stopPush(context.getApplicationContext());//停止推送
    }
}
