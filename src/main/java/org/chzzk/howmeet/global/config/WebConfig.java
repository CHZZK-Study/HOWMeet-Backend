package org.chzzk.howmeet.global.config;

import lombok.RequiredArgsConstructor;
import org.chzzk.howmeet.global.interceptor.AuthenticationInterceptor;
import org.chzzk.howmeet.global.interceptor.GuestAuthorityInterceptor;
import org.chzzk.howmeet.global.resolver.AuthPrincipalResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthPrincipalResolver authPrincipalResolver;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final GuestAuthorityInterceptor guestAuthorityInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
       registry.addInterceptor(authenticationInterceptor)
               .addPathPatterns("/**")
               .order(0);
       registry.addInterceptor(guestAuthorityInterceptor)
               .addPathPatterns("/**")
               .order(1);
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authPrincipalResolver);
    }

    // cors
}
