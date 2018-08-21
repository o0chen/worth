package com.blackeye.worth.vo;


import java.io.Serializable;


public class Result<T> implements Serializable {

    /**
     * 请求失败
     */
    public static final int FAIL = -1;
    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;
    /**
     * 没有权限
     */
    public static final int NO_PERMISSION = 2;

    /**
     * 登陆超时
     */
    public static final int LOGIN_TIMEOUT= 3;
    /**
     * 请求超时
     */
    public static final int REQUEST_TIMEOUT= 4;

    /**
     * 状态码
     */
    private int code = SUCCESS;

    /**
     * 是否成功
     */
    private Boolean isSuccess = true;

    /**
     * 消息
     */
    private String message = "success";

    /**
     * 要返回的数据
     */
    private T data;


    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static <T> Result ok(T data) {
        return new Builder().data(data).build();
    }

    public static <T> Result ok() {
        return new Builder().build();
    }

    public static Result error() {
        return error("error");
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        return result;
    }

    public Result(Result<T> result) {
//        ObjectUtils.requireNonNull(result);
//        if (ObjectUtils.nonNull(result.code)) {
//            this.code = result.code;
//        }
//        if (ObjectUtils.nonNull(result.message)) {
//            this.message = result.message;
//        }
//        if (ObjectUtils.nonNull(result.isSuccess)) {
//            this.isSuccess = result.isSuccess;
//        }
//        this.data = result.data;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", isSuccess=" + isSuccess +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    // Builder模式
    public static class Builder {
        private Result result;

        public Builder() {
            result = new Result();
        }

        public Builder code(int code) {
            result.code = code;
            return this;
        }

        public Builder isSuccess(Boolean isSuccess) {
            result.isSuccess = isSuccess;
            return this;
        }

        public Builder message(String message) {
            result.message = message;
            return this;
        }

        public <T> Builder data(T data) {
            result.data = data;
            return this;
        }

        public Result build() {
            return new Result(result);
        }
    }
}
