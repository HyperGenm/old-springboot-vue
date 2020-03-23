package org.mybatis.generator.plugin;

import com.weiziplus.springboot.common.util.DateUtils;
import com.weiziplus.springboot.common.util.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
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
            "sys_function", "sys_user_log", "sys_role", "sys_role_function", "sys_user","sys_error",
            "user_log");

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
        topLevelClass.addImportedType("com.weiziplus.springboot.common.base.Column");
        topLevelClass.addImportedType("com.weiziplus.springboot.common.base.Id");
        topLevelClass.addImportedType("com.weiziplus.springboot.common.base.Table");
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.experimental.Accessors");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        topLevelClass.addImportedType("org.apache.ibatis.type.Alias");
        //将字段设置为当前类的静态常量
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : allColumns) {
            String columnName = column.getActualColumnName();
            Field field = new Field("COLUMN_" + columnName.toUpperCase(), FullyQualifiedJavaType.getStringInstance());
            field.setInitializationString("\"" + columnName + "\"");
            field.setVisibility(JavaVisibility.PUBLIC);
            field.setFinal(true);
            field.setStatic(true);
            topLevelClass.addField(field);
        }
        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        //添加@注解
        topLevelClass.addAnnotation("@JsonInclude(JsonInclude.Include.NON_NULL)");
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Table(\"" + fullyQualifiedTable + "\")");
        topLevelClass.addAnnotation("@Accessors(chain = true)");
        topLevelClass.addAnnotation("@Alias(\"" + underlineToCamelHump(fullyQualifiedTable.toString()) + "\")");

        //设置类上面的注释
        topLevelClass.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" * " + remarkLine);
            }
            topLevelClass.addAnnotation("@ApiModel(\"" + remarks.replace("\r\n", " ") + "\")");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" * ").append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @author ").append(System.getProperties().getProperty("user.name"));
        topLevelClass.addJavaDocLine(sb.toString());
        sb.setLength(0);
        sb.append(" * @date ");
        sb.append(DateUtils.getNowDateTime());
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
            field.addAnnotation("@ApiModelProperty(\"" + remarks.replace("\r\n", " ") + "\")");
        }
        field.addJavaDocLine(" */");
        String actualColumnName = introspectedColumn.getActualColumnName();
        if (introspectedTable.getPrimaryKeyColumns().contains(introspectedColumn)) {
            field.addAnnotation("@Id(\"" + actualColumnName + "\")");
        } else {
            field.addAnnotation("@Column(\"" + actualColumnName + "\")");
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

    /**
     * 将下划线风格替换为驼峰风格---首字母大写
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
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = name.split(underline);
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰转为下划线
     *
     * @param str
     * @return
     */
    public String cam1eHumpToUnderline(String str) {
        return str.replaceAll("[A-Z]", "_$0").toUpperCase();
    }
}