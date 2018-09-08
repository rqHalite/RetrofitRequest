package com.rock.retrofitrequest.https;

/**
 * RxBus的bean
 */

public class MsgEvent<T> {
    private T data;
    private String mMsg;

    public MsgEvent(String mMsg) {
        this.mMsg = mMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

}
