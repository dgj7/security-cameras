package com.dg.securitycams.transcoder.rest;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.PipeOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/transcoder/ffmpeg")
public class FFMpegBasedSolution {
    @Value("${application.username}")
    private String username;
    @Value("${application.password}")
    private String password;
    @Value("${source.address}")
    private String address;
    @Value("${source.port}")
    private String port;

    @GetMapping(value="/live")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> live() {
        final String url = new StringBuffer()
                .append("rtsp://")
                .append(username)
                .append(":")
                .append(password)
                .append("@")
                .append(address)
                .append(":")
                .append(port)
                .append("/ISAPI/Streaming/channels/101/live")
                .toString();

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
                                    //1 frame every 10 seconds
                                    .setFrameRate(10)
                                    .setDuration(1, TimeUnit.HOURS)
                                    .setFormat("ismv"))
                            .addArgument("-nostdin")
                            .execute();
                });
    }
}
