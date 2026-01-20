package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer { // <--- 1. HEREDA DE AQUÍ

    // 2. SOBREESCRIBE ESTE MÉTODO (Es el "arranque" para Tomcat)
  /*  @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }
}*/
    // 3. El main se queda igual (para pruebas locales)
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }
}