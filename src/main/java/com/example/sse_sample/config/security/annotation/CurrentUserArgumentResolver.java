package com.example.sse_sample.config.security.annotation;

import com.example.sse_sample.config.security.entity.User;
import com.example.sse_sample.config.security.exception.BaseException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    if (parameter.getParameterAnnotation(CurrentUser.class) == null) {
      return false;
    }

    return parameter.getParameterType().equals(User.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new BaseException(); // TODO change
    }

    Object obj = authentication.getPrincipal();

    if (!(obj instanceof User)) {
      throw new BaseException(); // TODO change
    }

    return authentication.getPrincipal();
  }
}