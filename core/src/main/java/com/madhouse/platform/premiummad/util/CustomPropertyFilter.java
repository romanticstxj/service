package com.madhouse.platform.premiummad.util;

import com.alibaba.fastjson.serializer.PropertyFilter;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by zhujiajun
 * 15/11/3 14:11
 *
 * 使用Fastjson时,动态过滤字段
 *
 * example:
 *
 *  Map<Class<?>,List<String>> ignorePropertyMap = new HashMap<>();
 *  ignorePropertyMap.put(DemandAdspaceModel.class, Collections.singletonList("field"))
 *  or
 *  ignorePropertyMap.put(DemandAdspaceModel.class, Arrays.asList("field1","field2"));
 *
 */
public class CustomPropertyFilter implements PropertyFilter {

    private Map<Class<?>,List<String>> ignorePropertyMap;

    public CustomPropertyFilter(Map<Class<?>, List<String>> ignorePropertyMap) {
        Assert.notNull(ignorePropertyMap,"fieldMap cannot be null");
        this.ignorePropertyMap = ignorePropertyMap;
    }

    @Override
    public boolean apply(Object object, String name, Object value) {
        for (Map.Entry<Class<?>,List<String>> entry : ignorePropertyMap.entrySet()) {
            Class<?> aClass = entry.getKey();
            if (aClass == object.getClass()) {
                List<String> fieldList = entry.getValue();
                for (String field : fieldList) {
                    if (field.equals(name))
                        return false;
                }
            }

        }
        return true;
    }
}
