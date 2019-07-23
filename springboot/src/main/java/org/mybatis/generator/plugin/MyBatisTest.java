package org.mybatis.generator.plugin;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据数据库生成对应实体类文件
 *
 * @author wanglongwei
 * @data 2019/7/22 11:24
 */
public class MyBatisTest {

    /**
     * 若启动失败，请检查config/generator-config.xml里面jar路径和数据库配置信息
     *
     * @throws Exception
     */
    @Test
    public void main() throws Exception {
        File configFile = new File(MyBatisTest.class.getResource("/config/generator-config.xml").toURI());
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
