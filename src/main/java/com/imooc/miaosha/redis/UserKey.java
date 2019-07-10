package com.imooc.miaosha.redis;

/*
 * 用来获取User模块的,前缀信息
 */
public class UserKey extends BasePrefix{

	private UserKey(String prefix) {
		super(prefix);
	}
	public static UserKey getById = new UserKey("id");
	public static UserKey getByName = new UserKey("name");
}
