package org.evilincorporated.pineapple;

import org.evilincorporated.pineapple.security.service.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class PineappleProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PineappleProjectApplication.class, args);
    }

}
