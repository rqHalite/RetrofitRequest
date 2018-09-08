package com.rock.retrofitrequest.https;

/**
 * 解析返回的总结果
 */

public class Entity<T> {

    private int result;
    private String info;

    private T data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
