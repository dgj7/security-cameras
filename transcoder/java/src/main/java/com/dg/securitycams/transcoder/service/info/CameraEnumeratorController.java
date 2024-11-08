package com.dg.securitycams.transcoder.service.info;

import com.dg.securitycams.transcoder.model.camconfig.Camera;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dg.securitycams.transcoder.Constants.INFO_LIST_URI;

/**
 * /api/info/list/cameras
 */
@Slf4j
@RestController
@RequestMapping(INFO_LIST_URI)
public class CameraEnumeratorController {
    private final Map<String, Camera> cameraMap;

    public CameraEnumeratorController(final Map<String, Camera> cameras) {
        this.cameraMap = Objects.requireNonNull(cameras, "Map<String, Camera> is null");
        log.info("initialized: " + INFO_LIST_URI);
    }

    @GetMapping(value="/cameras")
    @ResponseBody
    public List<String> listCameras() {
        return ImmutableList.<String>builder()
                .addAll(cameraMap.keySet())
                .build();
    }
}
