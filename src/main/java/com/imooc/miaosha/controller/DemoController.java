package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {

		//注入server对象,调用里面的接口
	
		@Autowired
		UserService userService;
	
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
	 	
	 	@RequestMapping("/thymeleaf")
	    public String  thymeleaf(Model model) {
	 		model.addAttribute("name", "Joshua");
	 		return "hello";
	    }
	 	
	 	@RequestMapping("/db/get")
	    @ResponseBody
	    public Result<User> dbGet() {
	    	User user = userService.getById(1);
	        return Result.success(user);
	    }
	 	
	 	
	 	
}
