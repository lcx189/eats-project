package com.eats.config;

import com.eats.interceptor.JwtTokenAdminInterceptor;
import com.eats.interceptor.JwtTokenUserInterceptor;
import com.eats.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 設定クラス、Web層関連コンポーネントを登録
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * カスタムインターセプタを登録
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("カスタムインターセプタの登録を開始します...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    @Bean
    public Docket docket1(){
        log.info("APIドキュメントの生成を準備しています...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("EatsプロジェクトAPIドキュメント")
                .version("2.0")
                .description("Eats出前プロジェクトAPIドキュメント")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理側インターフェー")
                .apiInfo(apiInfo)
                .select()
                //生成するインターフェースがスキャンする必要のあるパッケージを指定
                .apis(RequestHandlerSelectors.basePackage("com.eats.controller.admin"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    @Bean
    public Docket docket2(){
        log.info("APIドキュメントの生成を準備しています...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Eats出前プロジェクトAPIドキュメント")
                .version("2.0")
                .description("Eats出前プロジェクトAPIドキュメント")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("ユーザー側インターフェー")
                .apiInfo(apiInfo)
                .select()
                //生成するインターフェースがスキャンする必要のあるパッケージを指定
                .apis(RequestHandlerSelectors.basePackage("com.eats.controller.user"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    /**
     * 静的リソースマッピングを設定し、主にAPIドキュメント（html、js、css）にアクセスしま
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静的リソースマッピングの設定を開始します...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Spring MVCフレームワークのメッセージコンバータを拡大
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("メッセージコンバータを拡張します...");
        //メッセージコンバータオブジェクトを作成します
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //メッセージコンバータには、JavaオブジェクトをJSONデータにシリアライズできるオブジェクトコンバータを設定する必要があります
        converter.setObjectMapper(new JacksonObjectMapper());
        //独自のメッセージコンバータをコンテナに追加します
        converters.add(0,converter);
    }
}
