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
 * @auth Exmay Team / 亿美团队 {tangxiangwei@exmay.com}
 *************************************************************************************
 * @date 2018年3月17日 下午1:10:58
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
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
	/**
     * 表示访问该方法的url
     * @return
     */
    String value() default "";
}
