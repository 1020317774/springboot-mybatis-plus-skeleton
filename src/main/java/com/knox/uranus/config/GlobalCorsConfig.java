package com.knox.uranus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置
 *
 * @author knox
 * @date 2020/7/27
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许所有域名进行跨域调用
                .allowedOriginPatterns("*")
                //spring5之后，上述配置会报错，请更换以下配置
                //.allowedOriginPatterns("*")
                //允许跨越发送cookie
                .allowCredentials(true)
                //放行全部原始头信息
                .allowedHeaders("*")
                //允许请求方法跨域调用，若放行所有，设置为 *
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                //预检请求的有效期，单位为秒。
                .maxAge(3600);
    }
}
