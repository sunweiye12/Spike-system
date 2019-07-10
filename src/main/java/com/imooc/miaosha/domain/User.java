package com.imooc.miaosha.domain;

/*
 * 创建一个doamain类与数据库中的user表对应
 */
public class User {
	//一共两个字段
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
