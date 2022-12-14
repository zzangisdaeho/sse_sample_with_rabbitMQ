package com.example.sse_sample.config.security.auth;

import com.example.sse_sample.config.security.auth.jwt.GlobalConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StaticPropertyInitializer {
//  @Value("${spring.profiles.active}")
//  protected String activeProfile;
//
//  @Value("${playv.alert-channel}")
//  protected String slackChannel;

  @Value("${playv.cookie.domain}")
  protected String cookieDomain;

  @Value("${playv.cookie.secure}")
  protected Boolean cookieSecure;

  @PostConstruct
  public void init() {
//    initSlack();
    initGlobalConstant();
  }

//  private void initSlack() {
//    Slack.setChannel(slackChannel);
//    Slack.setBotName("INVESTOR-" + activeProfile.toUpperCase(Locale.ROOT));
//    Slack.setCurrentLoggingLevel(loggingLevel);
//    Slack.setIsTest(false);
//  }

  private void initGlobalConstant() {
    GlobalConstant.cookieDomain = this.cookieDomain;
    GlobalConstant.cookieSecure = this.cookieSecure;
  }

}