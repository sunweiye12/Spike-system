package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;
/*
 * 注意:为何进行两次加密呢
 * 	1.第一次数库加密,是为了防止客户端传入的信息在网上明文传输,将信息加盐(固定盐)以后md5传输,这样即使其他人截获了密码,他能够通过反差彩虹表找到
 * 		加密前的信息,但是加密的信息里面包含盐值,特不知道加盐的方式就没有办法得到密码
 *  2.第二次加密是因为,担心数据库服务器端,发生密码泄露,如果不惊醒第二次加密的话,泄露以后内部人员可能知道加盐的方式,就可以通过
 *  	去除盐值来获取到真正的密码.
 *  	而第二次加密,是通过传输过来的信息,加上一个随机的盐值,进行md5加密,然后存储到数据库中,这样一旦数据库中的密码泄露.那他通过反差彩虹币
 *  	得到的也是加密后且增加可一段随机盐值的信息,他不知道随机的盐值,因此无法提取到密码
 */

public class MD5Util {

	//将输入的字符串镜像md5加密后返回
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	//盐值
	private static final String salt = "1a2b3c4d"; //是固定的,使得服务端知道每次传输盐值的内容
	
	//第一次md5加密,将用户输入的值加密后进行传输(用户输入的信息加盐,然后通过md5加密)
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);//12+str+c3
		System.out.println(str);
		return md5(str);
	}
	
	//将网络传输过来的密码,进行第二次加密,存放到存放到数据库中 (此次加盐的盐值可以存储到数据库中,因此是随机的,需要传入)
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	//将用户输入的密码转化为数据库存储的密码-->相当于将以上两步融合
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
//		System.out.println(inputPassToFormPass("*9"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//		System.out.println(formPassToDBPass(inputPassToFormPass("*9"), "1a2b3c4d"));
		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
	}
}
