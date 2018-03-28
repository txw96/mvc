package com.txw.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
	/**
	 * 表示给controller注册别名
	 * 
	 * @return
	 */
	String value() default "";
}
