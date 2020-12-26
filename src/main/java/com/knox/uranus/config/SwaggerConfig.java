package com.knox.uranus.config;

import com.knox.uranus.common.config.BaseSwaggerConfig;
import com.knox.uranus.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 *
 * @author knox
 * @date 2020/08/26
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.knox.uranus.modules")
                .title("SpringBoot Mybatis-Plus项目快速开发骨架")
                .description("项目骨架接口文档")
                .contactName("knox")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
