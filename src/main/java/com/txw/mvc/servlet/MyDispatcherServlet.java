package com.txw.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.txw.mvc.annotation.MyController;
import com.txw.mvc.annotation.MyRequestMapping;

/**
 * <pre>
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
 *          <pre>
 */
public class MyDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
	private static final String SCAN_PACKAGE = "scanPackage";
	public final static String UNICODE_UTF8 = "UTF-8";
	private Properties properties = new Properties();
	private List<String> classNames = new ArrayList<>();
	private Map<String, Object> ioc = new HashMap<>();
	private Map<String, Method> handlerMapping = new HashMap<>();
	private Map<String, Object> controllerMap = new HashMap<>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		// 1.加载配置文件
		doLoadConfig(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
		// 2.初始化所有相关联的类,扫描用户设定的包下面所有的类
		doScanner(properties.getProperty(SCAN_PACKAGE));
		// 3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v beanName-bean) beanName默认是首字母小写
		doInstance();
		// 4.初始化HandlerMapping(将url和method对应上)
		initHandlerMapping();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// 处理请求
			doDispatch(req, resp);
		} catch (Exception e) {
			resp.getWriter().write("500!! Server Exception");
		}

	}

	private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (handlerMapping.isEmpty()) {
			return;
		}
		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replace(contextPath, "").replaceAll("/+", "/");
		if (!this.handlerMapping.containsKey(url)) {
			resp.getWriter().write("404 NOT FOUND!");
			return;
		}
		Method method = this.handlerMapping.get(url);
		// 获取方法的参数列表
		Class<?>[] parameterTypes = method.getParameterTypes();
		// 获取请求的参数
		Map<String, String[]> parameterMap = req.getParameterMap();
		// 保存参数值
		Object[] paramValues = new Object[parameterTypes.length];
		// 方法的参数列表
		for (int i = 0; i < parameterTypes.length; i++) {
			// 根据参数名称，做某些处理
			String requestParam = parameterTypes[i].getSimpleName();

			if (requestParam.equals("HttpServletRequest")) {
				// 参数类型已明确，这边强转类型
				paramValues[i] = req;
				continue;
			}
			if (requestParam.equals("HttpServletResponse")) {
				paramValues[i] = resp;
				continue;
			}
			if (requestParam.equals("String")) {
				for (Entry<String, String[]> param : parameterMap.entrySet()) {
					String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
					// \[|\]
					paramValues[i] = value;
				}
			}
		}
		// 利用反射机制来调用
		try {
			method.invoke(this.controllerMap.get(url), paramValues);// obj是method所对应的实例
			                                                        // 在ioc容器中
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doLoadConfig(String location) {
		// 把web.xml中的contextConfigLocation对应value值的文件加载到留里面
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
			// 用Properties文件加载文件里的内容
			properties.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关流
			if (null != resourceAsStream) {
				try {
					resourceAsStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void doScanner(String packageName) {

		// 把所有的.替换成/
		String packageDirName = "/" + packageName.replace(".", "/");
		URL dirs = Thread.currentThread().getContextClassLoader().getResource(packageDirName);
		try {
			String filePath = URLDecoder.decode(dirs.getFile(), UNICODE_UTF8);
			File dir = new File(filePath);
			// 如果不存在或者 也不是目录就直接返回
			if (!dir.exists() || !dir.isDirectory()) {
				return;
			}
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					// 递归读取包
					doScanner(packageName + "." + file.getName());
				} else {
					String className = packageName + "." + file.getName().replace(".class", "");
					classNames.add(className);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doInstance() {
		if (classNames.isEmpty()) {
			return;
		}
		for (String className : classNames) {
			try {
				// 把类搞出来,反射来实例化(只有加@MyController需要实例化)
				System.out.println(className);
				Class<?> clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(MyController.class)) {
					ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.getDeclaredConstructor().newInstance());
				} else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	private void initHandlerMapping() {
		if (ioc.isEmpty()) {
			return;
		}
		try {
			for (Entry<String, Object> entry : ioc.entrySet()) {
				Class<? extends Object> clazz = entry.getValue().getClass();
				if (!clazz.isAnnotationPresent(MyController.class)) {
					continue;
				}
				// 拼url时,是controller头的url拼上方法上的url
				String baseUrl = "";
				if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
					MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
					baseUrl = annotation.value();
				}
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (!method.isAnnotationPresent(MyRequestMapping.class)) {
						continue;
					}
					MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
					String url = annotation.value();
					url = (baseUrl + "/" + url).replaceAll("/+", "/");
					handlerMapping.put(url, method);
					controllerMap.put(url, clazz.getDeclaredConstructor().newInstance());
					System.out.println(url + "," + method);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把字符串的首字母小写
	 * 
	 * @param name
	 * @return
	 */
	private String toLowerFirstWord(String name) {
		char[] charArray = name.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}
}
