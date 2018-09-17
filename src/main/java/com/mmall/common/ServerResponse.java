package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author LinZhiQ
 * @date 2018/9/8 20:05
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
// 保证序列化 json 的时候不返回 null 的属性
public class ServerResponse<T> implements Serializable {

    private Integer status;
    private String msg;
    private T data;

    private ServerResponse(Integer status){
        this.status = status;
    }

    private ServerResponse(Integer status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(Integer status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    // 使之不序列化
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(Integer errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
