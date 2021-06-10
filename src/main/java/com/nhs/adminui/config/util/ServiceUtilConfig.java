package com.nhs.adminui.config.util;

import com.nhs.adminui.service.ServiceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceUtilConfig {

    @Bean
    public ServiceUtil serviceUtil() {
        return new ServiceUtil();
    }
}
