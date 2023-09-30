package com.HNG.Stage5_First_PersonalTask.service;

import com.HNG.Stage5_First_PersonalTask.model.VideoResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@Service
public interface VideoServices {
    public String startRecording();
    public String uploadingVideo(MultipartFile video, String sessionId) throws IOException;

    public ResponseEntity<Resource> playVideo(String videoName) throws IOException;
    public List<VideoResponseDto> listOfVideo();
}
