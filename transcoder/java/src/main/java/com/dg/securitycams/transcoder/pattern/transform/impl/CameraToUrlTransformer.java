package com.dg.securitycams.transcoder.pattern.transform.impl;

import com.dg.securitycams.transcoder.pattern.transform.Transformer;
import com.dg.securitycams.transcoder.model.config.cam.Camera;

public class CameraToUrlTransformer implements Transformer<Camera, String> {
    @Override
    public String transform(final Camera camera) {
        return new StringBuffer()
                .append("rtsp://")
                .append(camera.getUsername())
                .append(":")
                .append(camera.getPassword())
                .append("@")
                .append(camera.getIpAddress())
                .append(":")
                .append(camera.getPort())
                .append("/ISAPI/Streaming/channels/101/live")
                .toString();
    }
}
