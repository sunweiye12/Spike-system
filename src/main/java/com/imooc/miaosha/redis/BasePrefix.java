package com.imooc.miaosha.redis;

/*
 * 此类为KeyPrefix借口的实现类,模板模式情况下,为我们的接口提供一下几种模板
 */

public abstract class BasePrefix implements KeyPrefix{
	
	private int expireSeconds; //过期时间
	private String prefix;	   //字符串前缀	
	
	public BasePrefix(String prefix) {//0代表永不过期
		this(0, prefix);
	}
	
	public BasePrefix( int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	//方法重写
	public int expireSeconds() {//默认0代表永不过期
		return expireSeconds;
	}

	//方法重写
	public String getPrefix() {
		String className = getClass().getSimpleName(); //获取当前的类名拼接上 UserKey:  (为了使不同模块的前缀不同)
		return className+":" + prefix;
	}

}
