package com.dg.securitycams.transcoder.model.config.app;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BuildInfo {
    @Getter
    @Setter
    private String commitId;
    @Getter
    @Setter
    private String commitIdAbbrev;
    @Getter
    @Setter
    private String project;
    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String description;
    @Getter
    private final List<String> activeSpringProfiles = new LinkedList<>();

    public String findFirstActiveSpringProfile() {
        return activeSpringProfiles
                .stream()
                .findFirst()
                .orElse("");
    }

    // todo: describable interface?
    public String describe() {
        final List<String> parts = ImmutableList.<String>builder()
                .add(version)
                .add(commitIdAbbrev)
                .add(findFirstActiveSpringProfile())
                .build()
                .stream()
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank)
                .toList();
        return title + " (" + String.join(",", parts) + ")";
    }
}
