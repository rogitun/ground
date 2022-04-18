package heading.ground.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheck())
                .order(1)
                .addPathPatterns("/messages","/edit/**","/profile/**","/book","/menus/**/edit","/menus/add**","/menus/manage")
                .excludePathPatterns("/files","/image","/styles","/uikit","/menus/[1-9]");
    }
}
