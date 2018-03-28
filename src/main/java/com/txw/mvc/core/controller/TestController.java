package com.txw.mvc.core.controller;
/**<pre>
 ************************************************************************************* 
 * [标题：类]
 ************************************************************************************* 
 * [描述：]
 ************************************************************************************* 
 * @auth txw Team / city团队 {18373282867@139.com}
 *************************************************************************************
 * @date 2018年3月25日 上午11:29:37
 *************************************************************************************
 * @version Version 1.0.0 Beta / v1.0.0 测试版
 ************************************************************************************* 
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
