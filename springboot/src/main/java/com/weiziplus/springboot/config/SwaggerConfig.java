package com.weiziplus.springboot.config;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 *
 * @author wanglongwei
 * @date 2019/4/22 16:04
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "global", value = {"swagger"}, havingValue = "true")
public class SwaggerConfig {
    @Bean
    public Docket apiDocument() {
        List<ResponseMessage> responseMessageList = new ArrayList<>(7);
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("success").build());
        responseMessageList.add(new ResponseMessageBuilder().code(400).message("缺少参数").build());
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("token错误").build());
        responseMessageList.add(new ResponseMessageBuilder().code(402).message("error").build());
        responseMessageList.add(new ResponseMessageBuilder().code(403).message("权限不足拒绝访问").build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到路径").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("系统错误").build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .globalResponseMessage(RequestMethod.OPTIONS, responseMessageList)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes());
    }

    /**
     * 设置swagger信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger2构建api文档")
                .description("Swagger的RESTful风格API")
                .version("1.0")
                .build();
    }

    /**
     * 设置右上角全局token
     *
     * @return
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList<>(1);
        apiKeyList.add(new ApiKey(GlobalConfig.TOKEN, GlobalConfig.TOKEN, "header"));
        return apiKeyList;
    }
}