package com.rock.retrofitrequest.https;

import android.content.Context;
import android.text.TextUtils;


import com.rock.retrofitrequest.url.NetTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * 用于请求方式的utils
 */

public class RetrofitUtil {

    private volatile static RetrofitUtil INSTANCE;
    private Retrofit mRetrofit;
    private ApiService mApiServiceStr;
    private ApiService mApiServiceGson;

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10;

    private RetrofitUtil() {
    }

    public static RetrofitUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 上传多张图片加文字加语音文件
     *
     * @param imageItems
     * @param rxSubscriber
     */
//    public Flowable<String> upBillImg(Context mContext, String voicePath, String voiceKey, String picflag, String url, List<BillImgBean> imageItems, Map<String, String> params, RxSubscriber<String> rxSubscriber) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);//表单类型
////             .addFormDataPart(ParamKey.TOKEN, token);//ParamKey.TOKEN 自定义参数key常量类，即参数名
//        for (String key : params.keySet()) {
//            Object object = params.get(key);
//            builder.addFormDataPart(key, object.toString());
//        }
//        if (voicePath != null && !TextUtils.isEmpty(voicePath)) { //录音文件
//            File file = new File(voicePath);//filePath 录音地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(voiceKey, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        //多张图片
//        for (int i = 0; i < imageItems.size(); i++) {
//            File file = new File(imageItems.get(i).smallPath);//filePath 图片地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(picflag + (i + 1), file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        Flowable<String> flowable = getServiceWithStr(mContext).upLoadImages(url, parts);
//        flowable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rxSubscriber);
//        return flowable;
//    }

    /**
     * 上传多张图片加文字
     *
     * @param imageItems
     * @param rxSubscriber
     */
//    public Flowable<String> upImg(Context mContext, String picflag, String url, List<BillImgBean> imageItems, Map<String, String> params, RxSubscriber<String> rxSubscriber) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);//表单类型
////             .addFormDataPart(ParamKey.TOKEN, token);//ParamKey.TOKEN 自定义参数key常量类，即参数名
//        for (String key : params.keySet()) {
//            Object object = params.get(key);
//            builder.addFormDataPart(key, object.toString());
//        }
//        if(imageItems!=null&&imageItems.size()!=0){
//            //多张图片
//            for (int i = 0; i < imageItems.size(); i++) {
//                File file = new File(imageItems.get(i).smallPath);//filePath 图片地址
//                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                builder.addFormDataPart(picflag + (i + 1), file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//            }
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        Flowable<String> flowable = getServiceWithStr(mContext).upLoadImages(url, parts);
//        flowable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rxSubscriber);
//        return flowable;
//    }

    /**
     * 上传一张图片
     *
     * @param mContext
     * @param url          地址
     * @param picKey       图片字段
     * @param imageUrl     图片路径
     * @param params       参数
     * @param rxSubscriber
     * @return
     */
//    public Flowable upLoadImage(Context mContext, String url, String picKey, String imageUrl, Map<String, String> params, RxSubscriber<String> rxSubscriber) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        for (String key : params.keySet()) {
//            Object object = params.get(key);
//            builder.addFormDataPart(key, object.toString());
//        }
//        if (imageUrl != null) {
//            File file = new File(imageUrl);//filePath 图片地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(picKey, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        Flowable<String> flowable = getServiceWithStr(mContext).upLoadImage(url, parts);
//        flowable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rxSubscriber);
//        // 执行
//        return flowable;
//    }

    /**
     * 个人认证 接口图片上传 （两个字段两张不同的图片，一字段一图片）
     *
     * @param mContext
     * @param url          接口URL
     * @param imgMap       存放两个不同字段的两张不同图片的Map
     * @param params       存放传递的文本参数的Map
     * @param rxSubscriber
     * @return
     */
//    public Flowable upLoadDifferentImage(Context mContext, String url, Map<String, String> imgMap, Map<String, String> params, RxSubscriber<String> rxSubscriber) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        for (String key : params.keySet()) { // 循环获取 文本Map中的所有内容
//            Object object = params.get(key);//根据 Map中key 获取到value
//            builder.addFormDataPart(key, object.toString());
//        }
//        if (!TextUtils.isEmpty(imgMap.get("idcard_pic1")) && !TextUtils.isEmpty(imgMap.get("idcard_pic2"))) {
//            for (String imgKey : imgMap.keySet()) {
//                File file = new File(imgMap.get(imgKey));//filePath 图片地址
//                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                builder.addFormDataPart(imgKey, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//            }
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        Flowable<String> flowable = getServiceWithStr(mContext).upLoadImage(url, parts);
//        flowable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rxSubscriber);
//        // 执行
//        return flowable;
//    }

    /**
     * 上传多张图片 + 文字 + 语音文件 + 单张图片
     *
     */
//    public Flowable<String> upDateSlills(Context mContext, String voicePath, String voiceKey, String picflag, String url, List<BillImgBean> imageItems,String picKey,String picValue , Map<String, String> params, RxSubscriber<String> rxSubscriber) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);//表单类型
////             .addFormDataPart(ParamKey.TOKEN, token);//ParamKey.TOKEN 自定义参数key常量类，即参数名
//        for (String key : params.keySet()) {
//            Object object = params.get(key);
//            builder.addFormDataPart(key, object.toString());
//        }
//        if (voicePath != null && !TextUtils.isEmpty(voicePath)) { //录音文件
//            File file = new File(voicePath);//filePath 录音地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(voiceKey, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        //多张图片
//        for (int i = 0; i < imageItems.size(); i++) {
//            File file = new File(imageItems.get(i).smallPath);//filePath 图片地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(picflag + (i + 1), file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        //  单张图片
//        if (TextUtils.isEmpty(picValue)) {
//            File file = new File(picValue);//filePath 图片地址
//            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            builder.addFormDataPart(picKey, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
//        }
//        List<MultipartBody.Part> parts = builder.build().parts();
//        Flowable<String> flowable = getServiceWithStr(mContext).upLoadImages(url, parts);
//        flowable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rxSubscriber);
//        return flowable;
//    }


    private OkHttpClient getClient(final Context mContext) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//错误重联
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        Request newRequest = builder.method(request.method(), request.body())
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .build();

        return client;
    }

    /**
     * 使用Gson解析
     *
     * @return
     */
    public ApiService getServiceWithGson(Context mContext) {
        if (mApiServiceGson == null) {
            mApiServiceGson = new Retrofit.Builder()
                    .baseUrl(NetTask.URL)
                    .client(getClient(mContext))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ApiService.class);
        }

        return mApiServiceGson;
    }

    /**
     * 不使用Gson解析
     *
     * @return
     */
    public ApiService getServiceWithStr(Context mContext) {
        if (mApiServiceStr == null) {
            mApiServiceStr = new Retrofit.Builder()
                    .baseUrl(NetTask.URL)
                    .client(getClient(mContext))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ApiService.class);
        }
        return mApiServiceStr;
    }

}
