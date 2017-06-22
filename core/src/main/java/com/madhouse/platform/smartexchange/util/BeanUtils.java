package com.madhouse.platform.smartexchange.util;

import com.madhouse.platform.smartexchange.annotation.NotNull;
import com.madhouse.platform.smartexchange.constant.SystemConstant;
import org.springframework.beans.FatalBeanException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BeanUtils {

	/**
	 * 得到list的大小
	 * 
	 * @return 2015年1月29日上午11:50:00
	 * @author xiejun
	 */
	public static int getListSize(List<?> list) {
		return list != null ? list.size() : 0;
	}

	/**
	 * 复制集合
	 * 要用普通for循环，用增强for时有时会出现并发异常
	 */
	public static <T, E> void copyList(List<T> source, List<E> target, Class<E> targetClass) {
		copyList(source,target,targetClass,(String[]) null);
	}

	public static <T, E> void copyList(List<T> source, List<E> target, Class<E> targetClass,String... ignoreProperties) {
		if (source != null && target != null) {
			for (int i = 0; i < source.size(); i++) {
				Object object = source.get(i);
				try {
					if (object != null) {
						E instance = targetClass.newInstance();
						org.springframework.beans.BeanUtils.copyProperties(object,instance,ignoreProperties);
						target.add(instance);
					}
				} catch (Exception e) {
					throw new FatalBeanException("Could not copy property from source to target", e);
				}
			}
		}
	}

	/**
	 * 避免程序中的导包麻烦，就单独提取出来
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		if (source != null && target != null) {
			org.springframework.beans.BeanUtils.copyProperties(source, target);
		}

	}

	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		if (source != null && target != null) {
			org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
		}
	}

	/**
	 * 根据测试文件解析生成指定类
	 * 
	 * @param clazz
	 * @param methodName
	 * @param filePath
	 * @return
	 */
	public static <T> T parseFileToObject(Class<T> clazz, String methodName, String filePath) {
		T obj = null;
		try {
			InputStream in = BeanUtils.class.getClassLoader().getResourceAsStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(inputStreamReader);

			obj = clazz.newInstance();

			boolean flag = false;

			String line = null;
			while ((line = reader.readLine()) != null) {

				if (line.equals("")) {
					flag = false;
				}
				if (flag) {
					String[] keyValue = line.split("=");
					if (keyValue.length != 2) {
						continue;
					}
					String key = keyValue[0].trim();
					String value = keyValue[1].trim();

					PropertyDescriptor descriptor = new PropertyDescriptor(key, obj.getClass());
					Method writeMethod = descriptor.getWriteMethod();

					Class<?> propertyType = descriptor.getPropertyType();
					if (propertyType.equals(Integer.class)) {
						writeMethod.invoke(obj, Integer.parseInt(value));
					} else if (propertyType.equals(String.class)) {
						writeMethod.invoke(obj, value);
					} else if (propertyType.equals(Float.class)) {
						writeMethod.invoke(obj, Float.parseFloat(value));
					} else if (propertyType.equals(boolean.class)) {
						writeMethod.invoke(obj, Boolean.parseBoolean(value));
					} else if (propertyType.equals(Date.class)) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						writeMethod.invoke(obj, format.parse(value));
					}
				}
				if (line.startsWith("methodName") && line.endsWith(methodName)) {
					flag = true;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 判断对象中加注解的字段是否为null,返回为null的字段名称
	 * 
	 * @param obj
	 * @return
	 */
	public static String hasEmptyField(Object obj) {
		if (obj != null) {
			Class<?> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			int length = fields.length;
			for (int i = 0; i < length; i++) {
				Field field = fields[i];
				NotNull notNull = field.getAnnotation(NotNull.class);
				String fieldName = field.getName();
				if (notNull != null) {
					// 如果有NotNull注解，就判断是否为null，如果为null就返回true
					field.setAccessible(true);
					Object value = null;
					try {
						value = field.get(obj);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
						throw new RuntimeException("field invoke exception,please check field:" + field.getName());
					}
					if (value == null || value.toString().trim().equals("")) {
						return fieldName;
					}
				} else {
					continue;
				}
			}
			return null;
		} else {
			return "object"; // 如果对象为null返回object，方便消息提示
		}
	}

	/*
	 * 判断list是否为null
	 */
	public static boolean isEmpty(List<?> list) {
		return (list == null || list.size() == 0);
	}

	/*
	 * 设置创建人，创建时间，更新人，更新时间
	 */
	public static void setCreateParam(Object obj) {
		setNeedParam(obj, null, false);
	}

	public static void setCreateParam(Object obj, String dataSource) {
		setNeedParam(obj, dataSource, false);
	}

	/*
	 * 设置更新人，更新时间
	 */
	public static void setUpdateParam(Object obj) {
		setNeedParam(obj, null, true);
	}

	public static void setUpdateParam(Object obj, String dataSource) {
		setNeedParam(obj, dataSource, true);
	}

	public static void setNeedParam(Object obj, String dataSource, boolean update) {
		if (obj != null) {
			Class<?> clazz = obj.getClass();
			try {
				Method createUserWriteMethod = new PropertyDescriptor("createUser", clazz).getWriteMethod();
				Method createTimeWriteMethod = new PropertyDescriptor("createTime", clazz).getWriteMethod();
				Method updateUserWriteMethod = new PropertyDescriptor("updateUser", clazz).getWriteMethod();
				Method updateTimeWriteMethod = new PropertyDescriptor("updateTime", clazz).getWriteMethod();

				Integer userId = null;
				Date currentTime = new Date();

				RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
				if (requestAttributes != null && ((ServletRequestAttributes) requestAttributes).getRequest() != null) {
					HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
					userId = Integer.parseInt(request.getHeader(SystemConstant.USERID));
				}

				/* 多数据源情况使用
				if (dataSource != null) {
					currentTime = DateUtils.getCurrentDate(dataSource);
				} else {
					currentTime = DateUtils.getCurrentDate();
				}
				*/

				if (createUserWriteMethod != null) {
					if (!update) {
						createUserWriteMethod.invoke(obj, userId);
					}
				}
				if (createTimeWriteMethod != null) {
					if (!update) {
						createTimeWriteMethod.invoke(obj, currentTime);
					}
				}
				if (updateUserWriteMethod != null) {
					updateUserWriteMethod.invoke(obj, userId);
				}
				if (updateTimeWriteMethod != null) {
					updateTimeWriteMethod.invoke(obj, currentTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("field invoke exception", e);
			}
		}
	}

	/*
	 * 去除对象中String的空格
	 */
	public static void copyParams(Object source, Object target, String... params) {
		if ((source != null) && (target != null)) {
			if (params != null) {
				for (String param : params) {
					try {
						Field field = source.getClass().getDeclaredField(param);
						field.setAccessible(true);
						Object sourceValue = field.get(source);
						if (sourceValue != null) {
							if (String.class.equals(sourceValue.getClass())) {
								String trimValue = sourceValue.toString().trim();
								field.set(target, trimValue);
							} else {
								field.set(target, sourceValue);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException("field invoke exception", e);
					}
				}
			}
		}
	}

}
