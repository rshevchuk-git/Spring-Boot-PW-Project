package com.bookly.backend.configs;

import com.bookly.backend.filters.SecurityFilter;
import com.bookly.backend.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class FiltersConfig {

    @Value(value = "${cors.urls}")
    private String corsUrls;

    private final UserService userService;

    public FiltersConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(getCorsUrls()));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Security-Token"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        source.registerCorsConfiguration("/v1/**", config);
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(new CorsFilter(source));
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilter() {
        FilterRegistrationBean<SecurityFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SecurityFilter(userService));
        registrationBean.addUrlPatterns("/v1/bookings/*", "/v1/users/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    private String[] getCorsUrls() {
        return Optional.ofNullable(corsUrls)
                .map(value -> value.split(","))
                .orElseGet(() -> new String[0]);
    }

}
