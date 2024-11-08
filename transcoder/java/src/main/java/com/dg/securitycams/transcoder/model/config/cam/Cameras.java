package com.dg.securitycams.transcoder.model.config.cam;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Cameras {
    @Getter
    private final List<Camera> cameras = new LinkedList<>();
}
