package com.spt.managesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author: songpintong
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})   //版本控制访问
public class SwaggerConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里一定要标注控制器的位置
                .apis(RequestHandlerSelectors.basePackage("com.spt.managesystem.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("公司内部管理系统")
                .description("公司内部管理系统接口文档")
                .contact(new Contact("宋品通","https://github.com/","121577625@qq.com"))
                .version("1.0")
                .build();
    }
}
