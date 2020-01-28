package com.weiziplus.springboot.common.config;

import com.weiziplus.springboot.common.util.ToolUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 将resultType类型为map的下划线转驼峰
 *
 * @author wanglongwei
 * @date 2019/7/19 10:57
 */
@Configuration
public class MybatisConfig {
    /**
     * mybatis resultType为map时下划线键值转小写驼峰形式插
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setObjectWrapperFactory(new MapWrapperFactory());
    }

    static class MapWrapperFactory implements ObjectWrapperFactory {
        @Override
        public boolean hasWrapperFor(Object object) {
            return object instanceof Map;
        }

        @Override
        public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
            return new MyMapWrapper(metaObject, (Map) object);
        }
    }

    static class MyMapWrapper extends MapWrapper {
        MyMapWrapper(MetaObject metaObject, Map<String, Object> map) {
            super(metaObject, map);
        }

        @Override
        public String findProperty(String name, boolean useCamelCaseMapping) {
            if (useCamelCaseMapping) {
                return underlineToCamelHump(name);
            }
            return name;
        }

        /**
         * 将下划线风格替换为驼峰风格
         *
         * @param name
         * @return
         */
        private String underlineToCamelHump(String name) {
            if (ToolUtils.isBlank(name)) {
                return "";
            }
            String underline = "_";
            // 不含下划线，仅将首字母小写
            if (!name.contains(underline)) {
                return name.substring(0, 1).toLowerCase() + name.substring(1);
            }
            StringBuilder result = new StringBuilder();
            // 用下划线将原始字符串分割
            String[] camels = name.split(underline);
            for (String camel : camels) {
                // 跳过原始字符串中开头、结尾的下换线或双重下划线
                if (camel.isEmpty()) {
                    continue;
                }
                // 处理真正的驼峰片段
                if (result.length() == 0) {
                    // 第一个驼峰片段，全部字母都小写
                    result.append(camel.toLowerCase());
                } else {
                    // 其他的驼峰片段，首字母大写
                    result.append(camel.substring(0, 1).toUpperCase());
                    result.append(camel.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }
    }
}