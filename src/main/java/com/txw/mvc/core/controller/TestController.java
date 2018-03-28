package com.txw.mvc.core.controller;
/**<pre>
 ************************************************************************************* 
 * [标题：类]
 ************************************************************************************* 
 * [描述：]
 ************************************************************************************* 
 * @auth Exmay Team / 亿美团队 {tangxiangwei@exmay.com}
 *************************************************************************************
 * @date 2018年3月17日 下午1:15:19
 *************************************************************************************
 * @version Version 1.0.0 Beta / v1.0.0 测试版
 ************************************************************************************* 
 * 
 * @exmay
 * ===================================================================================
 * 版权所有：Copyright  2018 Exmay Inc. All Rights Reserved. 湖南亿美科技有限公司 版权所有
 * -----------------------------------------------------------------------------------
 * 企业文化：专注 创新 分享 共赢
 * -----------------------------------------------------------------------------------
 * 官方网站：<a href="http://www.exmay.com">http://www.exmay.com/</a >
 * -----------------------------------------------------------------------------------
 * 官方论坛：<a href="http://bbs.exmay.com">http://bbs.exmay.com/</a >
 * -----------------------------------------------------------------------------------
 * 官方邮箱：<a href="mailto:service@exmay.com">service@exmay.com</a>
 * ===================================================================================
 * <pre>
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.txw.mvc.annotation.MyController;
import com.txw.mvc.annotation.MyRequestMapping;
import com.txw.mvc.annotation.MyRequestParam;

@MyController
@MyRequestMapping("/test")
public class TestController {

	@MyRequestMapping("/doTest")
	public void test1(HttpServletRequest request, HttpServletResponse response, @MyRequestParam("param") String param) {
		System.out.println(param);
		try {
			response.getWriter().write("doTest method success! param:" + param);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@MyRequestMapping("/doTest2")
	public void test2(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().println("doTest2 method success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
