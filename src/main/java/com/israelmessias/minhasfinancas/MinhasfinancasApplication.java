package com.israelmessias.minhasfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Configuration
@EnableWebMvc
public class MinhasfinancasApplication implements WebMvcConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry registry) 
    {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(MinhasfinancasApplication.class, args);
    }

}
