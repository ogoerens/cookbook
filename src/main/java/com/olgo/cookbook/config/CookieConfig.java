package com.olgo.cookbook.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.cookie")
@Getter
@Setter

public class CookieConfig {
    private boolean secure;
    private String sameSite;
    private String path;
}
