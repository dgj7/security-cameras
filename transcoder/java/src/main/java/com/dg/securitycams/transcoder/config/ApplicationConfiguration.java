package com.dg.securitycams.transcoder.config;

import com.dg.securitycams.transcoder.service.any.CallLogger;
import com.dg.securitycams.transcoder.pattern.transform.Transformer;
import com.dg.securitycams.transcoder.pattern.transform.impl.CameraToUrlTransformer;
import com.dg.securitycams.transcoder.model.config.cam.Camera;
import com.dg.securitycams.transcoder.model.config.cam.Cameras;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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
     * Generate urls.
     */
    @Bean
    public Transformer<Camera, String> urlGenerator() {
        return new CameraToUrlTransformer();
    }

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
                // todo: custom exception type here
                throw new RuntimeException("duplicate camera id: " + cam.getId());
            }
            map.put(cam.getId(), cam);
        });

        /* log that we've loaded the map; @Bean should only do this once */
        log.info("loaded: cameras.json");

        /* done */
        return map;
    }

    @Bean
    public CallLogger callLogger() {
        return new CallLogger(Logger::isDebugEnabled, (logger, format, args) -> logger.debug(format, args));
    }
}
