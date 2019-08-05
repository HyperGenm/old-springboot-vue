package org.mybatis.generator.plugin;

import com.weiziplus.springboot.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Arrays;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/7/22 11:22
 */
@Slf4j
public class MyBatisPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    /**
     * 忽略的数据库表
     */
    private final List<String> IGNORE_TABLE = Arrays.asList(
            "data_dictionary", "data_dictionary_value",
            "sys_function", "sys_log", "sys_role", "sys_role_function", "sys_user", "sys_abnormal_ip");

    /**
     * 设置类的注释
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (IGNORE_TABLE.contains(String.valueOf(introspectedTable.getFullyQualifiedTable()))) {
            return false;
        }
        //添加import
        topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonInclude");
        topLevelClass.addImportedType("com.weiziplus.springboot.base.Column");
        topLevelClass.addImportedType("com.weiziplus.springboot.base.Id");
        topLevelClass.addImportedType("com.weiziplus.springboot.base.Table");
        topLevelClass.addImportedType("lombok.Data");

        //添加@注解
        topLevelClass.addAnnotation("@JsonInclude(JsonInclude.Include.NON_NULL)");
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Table(\"" + introspectedTable.getFullyQualifiedTable() + "\")");

        //设置类上面的注释
        topLevelClass.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" * " + remarkLine);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" * ").append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(System.getProperties().getProperty("user.name"));
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(DateUtil.getNowDateTime());
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

    /**
     * 设置字段的注释
     *
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        field.addJavaDocLine("/**");
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        field.addJavaDocLine(" */");
        if (introspectedTable.getPrimaryKeyColumns().contains(introspectedColumn)) {
            field.addAnnotation("@Id(\"" + introspectedColumn.getActualColumnName() + "\")");
        } else {
            field.addAnnotation("@Column(\"" + introspectedColumn.getActualColumnName() + "\")");
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }
}