package com.imooc.miaosha.redis;

/*
 * 此接口用于在调用redis服务器存取数据的时候,设置存储的属性
 */
public interface KeyPrefix {
		
	public int expireSeconds();  //设置过期时间
	
	//在原来key的基础上加上这个前缀,作为真正的key存储在redis缓存中
	//不同不能前缀不同,这样易于管理,不易发生冲突共同
	public String getPrefix();	
	
}
