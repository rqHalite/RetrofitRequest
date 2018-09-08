package com.rock.retrofitrequest.https;

import android.content.Context;


import com.rock.retrofitrequest.app.MyApp;
import com.rock.retrofitrequest.https.download.FileDownLoadObserver;
import com.rock.retrofitrequest.https.download.RetrofitCallback;
import com.rock.retrofitrequest.url.NetTask;
import com.rock.retrofitrequest.utils.CheckResultUtil;
import com.rock.retrofitrequest.utils.OtherUtil;

import org.json.JSONObject;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 针对服务器返回的结果进行封装处理
 */

public class HttpUtil {

    private volatile static HttpUtil INSTANCE;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpUtil();
                }
            }
        }
        return INSTANCE;
    }

    public <T> void toSubscribe(final Context mContext, Flowable<Entity<T>> flowable, RxSubscriber<Entity<T>> rxSubscriber) {

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Entity<T>>() {
                    @Override
                    public boolean test(@NonNull Entity<T> mData) throws Exception {
                        boolean isFilter = false;
                        if (mData == null) {
//                            WCApplication.T(mContext, mData.getInfo());
                        } else if (mData.getResult() == 200) {
                            isFilter = true;
                        } else if (mData.getResult() == -1) {//异地登录
                            OtherUtil.cleanInfo(mContext);
                            MyApp.T(mContext, mData.getInfo());
                            CheckResultUtil.onLoginFailure(mContext);
                        } else if (mData.getResult() == 700) {//认证失败或未认证
                            isFilter = true;
                        } else {
                            MyApp.T(mContext, mData.getInfo());
                        }

                        return isFilter;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Entity<T>, Entity<T>>() {
                    @Override
                    public Entity<T> apply(@NonNull Entity<T> mData) throws Exception {
                        //Log.e("=======", "map" + mData.getContent());
                        return mData;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxSubscriber);
    }

    public void toSubscribeNoGson(final Context mContext, Flowable<String> flowable, RxSubscriber<String> rxSubscriber) {

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String mData) throws Exception {
//                        BaseActivity.onLoadEnd();
                        boolean isFilter = false;
                        if (mData == null) {
                            MyApp.T(mContext, "数据异常");
                            return isFilter;
                        }

                        JSONObject jsonObject = new JSONObject(mData);
                        String status = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        if ("200".equals(status) || "1".equals(status)) { //成功
                            isFilter = true;
                        } else if ("201".equals(status)) {
                            isFilter = true;
                        } else if ("202".equals(status)) {
                            isFilter = true;
                        } else if ("203".equals(status)) {
                            isFilter = true;
                        } else if ("204".equals(status)) {
                            isFilter = true;
                        } else if ("101".equals(status)) {
                            isFilter = true;
                        } else if ("102".equals(status)) {
                            isFilter = true;
                        } else if ("802".equals(status)) {
                            isFilter = true;
                        } else if ("801".equals(status)) {
                            isFilter = true;
                        } else if ("-1".equals(status)) { //异地登录
                            OtherUtil.cleanInfo(mContext);
                            MyApp.T(mContext, msg);
                            CheckResultUtil.onLoginFailure(mContext);
                        } else {
                            MyApp.T(mContext, msg);
                        }
                        return isFilter;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String mData) throws Exception {
                        return mData;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxSubscriber);
    }

    /**
     * 下载（回调DownProgress）
     *
     * @param suffixUrl
     * @return
     */
    /**
     * 下载单文件，该方法不支持断点下载
     *
     * @param url                  文件地址
     * @param destDir              存储文件夹
     * @param fileName             存储文件名
     * @param fileDownLoadObserver 监听回调
     */
    public void downloadFile(@NonNull String url, final String destDir, final String fileName, final FileDownLoadObserver<File> fileDownLoadObserver) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(NetTask.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(ApiService.class)
                .downLoadFile(url)
                .subscribeOn(Schedulers.io())//subscribeOn和ObserOn必须在io线程，如果在主线程会出错
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())//需要
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        fileDownLoadObserver.onDownLoadSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownLoadFail(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        fileDownLoadObserver.onComplete();
                    }
                });
    }

    public <T> ApiService getRetrofitService(final RetrofitCallback<T> callback) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        clientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//
//                okhttp3.Response response = chain.proceed(chain.request());
//                //将ResponseBody转换成我们需要的FileResponseBody
//                return response.newBuilder().body(new FileResponseBody<T>(response.body(), callback)).build();
//            }
//        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetTask.URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        return service;
    }

}
