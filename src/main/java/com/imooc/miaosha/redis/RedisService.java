package com.imooc.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/*
 * 此类用于提供redis可以提供的各种方法
 */
@Service    //在Service层必须标注上次注解才能够被springboot感知
public class RedisService {

	@Autowired
	JedisPool jedisPool; //注入JedisPool对象
	
	/**
	 * 在redis中获取一个对象  (根据输入的string 和 类名 返回对象)
	 * */
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource(); //通过连接池获取一个jedis客户端
			 //生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 String str = jedis.get(realKey);   //通过string类型的key get出来一个json对象
			 T t = stringToBean(str, clazz);	//将json类型转换成那个对象类型
			 return t;
		 }finally {
			 jedis.close(); //将其放回连接池
		 }
	}
	
	/**
	 * 对象存储到redis中  (输入为对象)
	 * */
	public <T> boolean set(KeyPrefix prefix, String key, T value) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String str = beanToString(value);  //将对象转换成json类型数据
			 if(str == null || str.length() <= 0) {
				 return false;
			 }
			
			 String realKey  = prefix.getPrefix() + key; //生成真正存储的key
			 int seconds =  prefix.expireSeconds();  	//过期时间
			 if(seconds <= 0) {
				 jedis.set(realKey, str);
			 }else {
				 jedis.setex(realKey, seconds, str);
			 }
			 return true;
		 }finally {
			 jedis.close(); //将其放回连接池
		 }
	}
	
	/**
	 * 判断key是否存在
	 * */
	public <T> boolean exists(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.exists(realKey); //判断在redis中是否存在,返回boolean类型对象
		 }finally {
			 jedis.close(); //将其放回连接池
		 }
	}
	
	/**
	 * 增加值 --> (使对应key的值加1)(若果不是数值类型第一次操作会变成0)
	 * */
	public <T> Long incr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.incr(realKey);
		 }finally {
			 jedis.close(); //将其放回连接池
		 }
	}
	
	/**
	 * 减少值 --> (使对应key的值减1)
	 * */
	public <T> Long decr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.decr(realKey);
		 }finally {
			 jedis.close(); //将其放回连接池
		 }
	}
	
	
	//序列化方法
	private <T> String beanToString(T value) {
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			 return ""+value;
		}else if(clazz == String.class) {
			 return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}
	}

	//反序列化方法
	@SuppressWarnings("unchecked")
	private <T> T stringToBean(String str, Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null) {
			 return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			 return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			 return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return  (T)Long.valueOf(str);
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

}
