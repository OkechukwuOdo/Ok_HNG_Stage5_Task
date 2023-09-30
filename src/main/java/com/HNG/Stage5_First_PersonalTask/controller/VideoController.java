package com.HNG.Stage5_First_PersonalTask.controller;

import com.HNG.Stage5_First_PersonalTask.service.VideoServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class VideoController {
    private final VideoServices videoServices;
    public String startRecording(){
        return null;
    }
}
