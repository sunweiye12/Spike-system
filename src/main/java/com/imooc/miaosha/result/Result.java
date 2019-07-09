package com.imooc.miaosha.result;

public class Result<T> {
	
	private int code;
	private String msg;
	private T data;

	/**
	 * 成功时候的调用(走构造1)
	 * */
	public static <T> Result<T> success(T data){
		return new Result<T>(data);
	}
	
	/**
	 * 失败时候的调用(走构造2)
	 * */
	public static <T> Result<T> error(CodeMsg cm){
		return new Result<T>(cm);
	}
	
	//构造函数1  --> 成功调用,直接将数据传入
	private Result(T data) {	
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	
	//构造函数2  --> 失败调用,传入失败的信息
	private Result(CodeMsg cm) {
		if(cm == null) {
			return;
		}
		this.code = cm.getCode(); //CodeMsg中封装了code属性和msg属性
		this.msg = cm.getMsg();
	}

	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public T getData() {
		return data;
	}
}
