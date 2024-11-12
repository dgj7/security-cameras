package com.dg.securitycams.transcoder.service.live;

import com.dg.securitycams.transcoder.pattern.transform.Transformer;
import com.dg.securitycams.transcoder.model.config.cam.Camera;
import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.PipeOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.dg.securitycams.transcoder.Constants.FFMPEG_URI;

@Slf4j
@RestController
@RequestMapping(FFMPEG_URI)
public class FFMpegBasedSolution {
    private final Map<String, Camera> cameraMap;
    private final Transformer<Camera, String> urlGen;

    public FFMpegBasedSolution(final Map<String, Camera> cameras, final Transformer<Camera, String> urlGenerator) {
        this.cameraMap = Objects.requireNonNull(cameras, "Map<String, Camera> is null");
        this.urlGen = Objects.requireNonNull(urlGenerator, "Transformer<Camera, String> is null");
        log.info("initialized: " + FFMPEG_URI);
    }

    @GetMapping(value="/{id}/live.mp4")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> live(@PathVariable final String id) {
        /* get the associated camera */
        final Camera camera = cameraMap.get(id);
        if (camera == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "can't find camera with id [" + id + "]");
        }

        /* build the url based on the camera configuration */
        final String url = urlGen.transform(camera);

        /* log a message */
        log.info("launching stream: [{}]...", camera.getId());

        /* call ffmpeg to generate stream */
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(os -> {
                    FFmpeg.atPath()
                            .addArgument("-re")
                            .addArguments("-acodec", "pcm_s16le")
                            .addArguments("-rtsp_transport", "tcp")
                            .addArguments("-i", url)
                            .addArguments("-vcodec", "copy")
                            .addArguments("-af", "asetrate=22050")
                            .addArguments("-acodec", "aac")
                            .addArguments("-b:a", "96k" )
                            .addOutput(PipeOutput.pumpTo(os)
                                    .disableStream(StreamType.AUDIO)
                                    .disableStream(StreamType.SUBTITLE)
                                    .disableStream(StreamType.DATA)
                                    .setFrameCount(StreamType.VIDEO, 100L)
                                    .setFrameRate(10)
                                    .setDuration(1, TimeUnit.HOURS)
                                    .setFormat("ismv"))
                            .addArgument("-nostdin")
                            .execute();
                });
    }
}
