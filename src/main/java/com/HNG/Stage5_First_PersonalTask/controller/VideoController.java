package com.HNG.Stage5_First_PersonalTask.controller;

import com.HNG.Stage5_First_PersonalTask.model.VideoResponseDto;
import com.HNG.Stage5_First_PersonalTask.service.VideoServices;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoServices videoServices;
    public String startRecording(){
        return null;
    }
@PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("video") MultipartFile video) throws IOException {
        videoServices.uploadingVideo( video);
        return null;
    }
    @GetMapping("/play/{videoTitle}")
    public ResponseEntity<Resource> getVideo(@PathVariable("videoTitle") String videoFileName) throws IOException {
        Resource videoResources = videoServices.playVideo(videoFileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(videoResources.contentLength());

        // Use ResponseEntity to stream the video
        return new ResponseEntity<>(videoResources, headers, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<VideoResponseDto>> allVideos(){
        List<VideoResponseDto> allVideo= videoServices.listOfVideo();
        return ResponseEntity.ok(allVideo);
    }
}
