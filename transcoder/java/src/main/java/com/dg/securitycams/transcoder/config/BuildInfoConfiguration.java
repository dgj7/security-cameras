package com.dg.securitycams.transcoder.config;

import com.dg.securitycams.transcoder.model.config.app.BuildInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Configuration
public class BuildInfoConfiguration {
    private final BuildProperties buildProperties;

    @Value("${git.commit.id}")
    private String commitId;
    @Value("${git.commit.id.abbrev}")
    private String commitIdAbbrev;
    @Value("${spring.profiles.active:unknown}")
    private String activeSpringProfilesCsv;

    @Autowired
    public BuildInfoConfiguration(final BuildProperties pBuildProperties) {
        this.buildProperties = Objects.requireNonNull(pBuildProperties, "BuildProperties is null");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocation(new ClassPathResource("git.properties"));
        pspc.setIgnoreResourceNotFound(true);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        return pspc;
    }

    @Bean
    public BuildInfo buildInfo() {
        final BuildInfo buildInfo = new BuildInfo();

        buildInfo.setCommitId(commitId);
        buildInfo.setCommitIdAbbrev(commitIdAbbrev);

        buildInfo.setProject(buildProperties.getName());
        buildInfo.setVersion(buildProperties.getVersion());

        Arrays.stream(Optional.ofNullable(activeSpringProfilesCsv).orElse("").split(","))
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotEmpty)
                .forEach(profile -> buildInfo.getActiveSpringProfiles().add(profile));

        buildInfo.setTitle("Transcoder");
        buildInfo.setDescription("transcodes webcam feeds");

        return buildInfo;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> appReadyListener(final BuildInfo bi) {
        return event -> log.info("{} ready for requests.", bi.describe());
    }
}
