package com.rock.retrofitrequest.https;


import com.rock.retrofitrequest.url.NetTask;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * 接口字段
 */

public interface ApiService {

    //注册验证码
//    @GET(URL.REGINSTER_CODE)  “1 注册”“2 修改密码”
    @FormUrlEncoded
    @POST
    Flowable<String> getRegisterCode(@Url String url, @FieldMap Map<String, String> map);

    //注册
    @FormUrlEncoded
    @POST
    Flowable<String> getRegister(@Url String url, @FieldMap Map<String, String> map);

    //手机快速登录
    @FormUrlEncoded
    @POST
    Flowable<String> getfastLogin(@Url String url, @FieldMap Map<String, String> map);

    // 登录页面
    @FormUrlEncoded
    @POST
    Flowable<String> getLogin(@Url String url, @FieldMap Map<String, String> map);

    @Streaming
    @GET
    Flowable<ResponseBody> downLoadFile(@NonNull @Url String url);

//    //我的订单列表
//    @FormUrlEncoded
//    @POST()
//    Flowable<Entity<MyOrderListBean>> getMyOrderList(@Url() String url, @FieldMap Map<String, String> map);
//
    //我的模块
    //籍贯选择
@POST(NetTask.first_URL)
Flowable<String> getSearchSkillsType();
//
//
//    //发单同意
//    @FormUrlEncoded
//    @POST(BaseNetTasks.MY_ORDER_ACCEPT)
//    Flowable<Entity<AcceptMyOrderBean>> getAcceptOrder(@FieldMap Map<String, String> map);
//
//    //支付优惠券列表
//    @FormUrlEncoded
//    @POST
//    Flowable<Entity<PayCouponBean>> getPayCoupon(@Url String url, @FieldMap Map<String, String> map);
//
//
//    //意见反馈
//    @FormUrlEncoded
//    @POST
//    Flowable<String> getFeedBack(@Url String url, @FieldMap Map<String, String> map);
}
