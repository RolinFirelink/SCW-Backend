package com.rolin.orangesmart.config;

import com.rolin.orangesmart.util.SwaggerUtil;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public GroupedOpenApi groupedOpenApi() {
    return SwaggerUtil.buildGroupedOpenApi("orange", "com.rolin.orangesmart");
  }
}