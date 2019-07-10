package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.domain.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao; //注入对象
	
	//通过调用DAO的接口来很具id获取user对象
	public User getById(int id) {
		 return userDao.getById(id);
	}

	//测试事务,出错回滚 --> (添加此标签以后如果出错会回滚)
//	@Transactional
	public boolean tx() {
		User u1= new User();
		u1.setId(2);
		u1.setName("2222");
		userDao.insert(u1);  //先插入id=2 成功
		
		User u2= new User();
		u2.setId(1);
		u2.setName("11111"); //有插入id=1会失败
		userDao.insert(u2);
		
		return true;
	}
	
}
