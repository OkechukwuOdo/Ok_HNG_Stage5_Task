package com.HNG.Stage5_First_PersonalTask.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class VideoResponseDto {
    private String name;
    private String videoPath;
}
