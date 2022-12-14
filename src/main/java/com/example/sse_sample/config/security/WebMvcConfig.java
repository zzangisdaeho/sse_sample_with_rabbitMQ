package com.example.sse_sample.config.security;

import com.example.sse_sample.config.security.annotation.CurrentUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${cors.allowed-origins}")
  private String[] origins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
//        .allowedOrigins(origins)
        .allowedOrigins("*")
        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
        .allowCredentials(true);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(new CurrentUserArgumentResolver());
  }
}