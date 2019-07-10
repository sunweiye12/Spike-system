package com.imooc.miaosha.dao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.imooc.miaosha.domain.User;

@Mapper
public interface UserDao {
	
	//配置mybatis的执行语句,返回查询后的结果(在到层直接和数据库交互.在server层需要调用dao提供的接口)
	@Select("select * from user where id = #{id}")
	public User getById(@Param("id")int id	);

	//插入一条信息
	@Insert("insert into user(id, name)values(#{id}, #{name})")
	public int insert(User user);
	
}
