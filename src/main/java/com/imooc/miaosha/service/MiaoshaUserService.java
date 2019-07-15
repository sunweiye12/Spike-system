package com.imooc.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;

@Service
public class MiaoshaUserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	@Autowired
	RedisService redisService;
	
	//查询此id是否存在
	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}
	

	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		//从缓存中获取cookie信息
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		//延长有效期
		if(user != null) {
			//从新添加一遍,更新过期时间
			addCookie(response, token, user); 
		}
		return user;
	}
	
	//验证输入的用户名密码是否正确
	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在//调用方法实现
		MiaoshaUser user = getById(Long.parseLong(mobile)); //调用本地方法
		if(user == null) { //如果查询不到,则返回错误
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码(获取数据库中的密码和盐值)
		String dbPass = user.getPassword();
		String dbSalt = user.getSalt();
		//通过传入的一次md5加密的密码,拿到数据库的盐值后得到最终加密的算法
		String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
		if(!calcPass.equals(dbPass)) { //如果不相等则返回错误
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//验证成功生成cookie(就是sessionid),存储到redis中,每次只需要拿到token就可以在缓存中得到用户信息
		String token = UUIDUtil.uuid();
		addCookie(response, token, user); //调用本地方法添加cookie信息到redis
		return true;
	}
	
	//为验证成功的用户添加cookie信息
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
		redisService.set(MiaoshaUserKey.token, token, user);  //将对应信息添加到redis缓存中
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);  //COOKI_NAME_TOKEN = "token"; 
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds()); //设置过期时间
		cookie.setPath("/");
		response.addCookie(cookie); //将cookie信息写入到客户端
	}

}
