package com.dg.securitycams.transcoder.config;

import com.dg.securitycams.transcoder.model.camconfig.Camera;
import com.dg.securitycams.transcoder.model.camconfig.Cameras;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.dg.securitycams.transcoder.Constants.CAMERAS_CONFIG_PATH;

/**
 * Main application configuration(s).
 */
@Slf4j
@Configuration
public class ApplicationConfiguration {
    /**
     * Load all configured cameras from {@code /src/main/resources/cameras.json}.
     */
    @Bean
    public Map<String, Camera> cameras() throws IOException {
        /* load the cameras from json configuration file */
        final ObjectMapper objectMapper = new ObjectMapper();
        final URL url = Objects.requireNonNull(ApplicationConfiguration.class.getResource(CAMERAS_CONFIG_PATH), "URL null for path [" + CAMERAS_CONFIG_PATH+ "]");
        final String json = Resources.toString(url, Charset.defaultCharset());
        final Cameras cameras = objectMapper.readValue(json, Cameras.class);

        /* load map with data */
        final Map<String, Camera> map = new HashMap<>();
        cameras.getCameras().forEach(cam -> {
            if (map.containsKey(cam.getId())) {
                throw new RuntimeException("duplicate camera id: " + cam.getId());
            }
            map.put(cam.getId(), cam);
        });

        /* log that we've loaded the map; @Bean should only do this once */
        log.info("loaded: cameras.json");

        /* done */
        return map;
    }
}
