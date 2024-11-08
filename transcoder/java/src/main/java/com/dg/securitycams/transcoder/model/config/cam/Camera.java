package com.dg.securitycams.transcoder.model.config.cam;

import lombok.Getter;
import lombok.Setter;

public class Camera {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String ipAddress;
    @Getter
    @Setter
    private String port;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
}
