package com.imooc.miaosha.util;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;

/*
 * 此类是用于验证手机号是否符合格式
 */
public class ValidatorUtil {
	
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	
//	public static void main(String[] args) {
//			System.out.println(isMobile("18912341234"));
//			System.out.println(isMobile("1891234123"));
//	}
}
