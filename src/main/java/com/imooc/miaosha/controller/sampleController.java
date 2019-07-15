package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.KeyPrefix;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;

@Controller
@RequestMapping("/demo")
public class sampleController {

		//注入server对象,调用里面的接口
		@Autowired
		UserService userService;
		
		//注入RedisServer对象,调用Redis的服务
		@Autowired
		RedisService redisService;
	
	 	@RequestMapping("/")
	    @ResponseBody
	    String home() {
	        return "Hello World!";
	    }
	 	
	 	//!!!!***对象信息会以json形式,展示在页面上***!!!!
	 	@RequestMapping("/hello")
	    @ResponseBody
	    public Result<String> hello() {
	 		return Result.success("hello正常数据信息!!"); //正常传入data信息
	       // return new Result(0, "success", "hello正常数据信息!!");
	    }
	 	
	 	@RequestMapping("/helloError")
	    @ResponseBody
	    public Result<String> helloError() {
	 		return Result.error(CodeMsg.SERVER_ERROR);	//异常传入错误对象
	 		//return new Result(500100, "XXX");
	    }
	 	
	 	//此处是将信息传递给网页
	 	@RequestMapping("/thymeleaf")
	    public String thymeleaf(Model model) {  
	 		model.addAttribute("name", "Joshua");
	 		return "hello";
	    }
	 	
	 	//测试数据库中取一条数据
	 	@RequestMapping("/db/get")
	    @ResponseBody
	    public Result<User> dbGet() {
	    	User user = userService.getById(1);
	        return Result.success(user);
	    }
	 	
	 	//测试服务接口中的事务
	 	@RequestMapping("/db/tx")
	    @ResponseBody
	    public Result<Boolean> dbTx() {
	    	userService.tx(); //执行这个会出错,但是包含事务的操作,检测会不会回滚
	        return Result.success(true);
	    }
	 	
	 	//执行redis提供的服务接口 ( 在redis中通过key来获取指定的对象 )
	 	@RequestMapping("/redis/get")
	    @ResponseBody
	    public Result<User> redisGet() {
	 		User v1 = redisService.get(UserKey.getById,"1",User.class); // UserKey:id1
	        return Result.success(v1);
	    }
	 	
	 	//执行redis提供的服务接口 ( 在redis中添加一个对象 )
	 	@RequestMapping("/redis/set")
	    @ResponseBody
	    public Result<Boolean> redisSet() {
	 		User user = new User();
	 		user.setId(1);
	 		user.setName("张山");
	 		boolean v2 = redisService.set(UserKey.getById,"1",user); 
	        return Result.success(v2);
	    }
	 	
}
