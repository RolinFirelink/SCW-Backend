package com.rolin.orangesmart.util;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;


public class SwaggerUtil {

  public static GroupedOpenApi buildGroupedOpenApi(String group, String path) {
    return GroupedOpenApi.builder()
            .packagesToScan(path)
            .pathsToMatch("/**")
            .group(group)
//            .addOpenApiCustomizer(globalHeaderCustomizer())
            .build();
  }

//  private static OpenApiCustomizer globalHeaderCustomizer() {
//    return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
//            .forEach(operation -> {
//              operation.addParametersItem(
//                      new HeaderParameter().name("language").description("语言").required(true).schema(new StringSchema()._default("zh-CN"))
//              );
//              operation.addParametersItem(
//                      new HeaderParameter().name("Authorization").description("token").required(true).schema(new StringSchema()._default("Bearer "))
//              );
//              operation.addParametersItem(
//                      new HeaderParameter().name("appName").description("appName").required(true).schema(new StringSchema()._default("msp"))
//              );
//              operation.addParametersItem(
//                      new HeaderParameter().name("ignoreScy").description("忽略权限校验").required(true).schema(new StringSchema()._default("true"))
//              );
//            });
//  }

}