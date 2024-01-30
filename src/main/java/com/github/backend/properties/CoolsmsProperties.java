package com.github.backend.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "message")
public class CoolsmsProperties {
    private String apikey;
    private String apiSecret;
    private String phoneNum;
}
