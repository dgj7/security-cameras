package com.dg.securitycams.transcoder.config;

import com.dg.securitycams.transcoder.model.config.app.BuildInfo;
import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @see http://localhost:8401/swagger-ui/index.html
 */
@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI apiInfo(final BuildInfo buildInfo) {
        return new OpenAPI()
                .info(new Info()
                        .title(buildInfo.getTitle())
                        .description(buildInfo.describe())
                        .version(buildInfo.getVersion()))
                .servers(servers(buildInfo));
    }

    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/**")
                .build();
    }

    private List<Server> servers(final BuildInfo buildInfo) {
        return ImmutableList.<Server>builder().build();
    }
}
