package com.HNG.Stage5_First_PersonalTask.service;

import com.HNG.Stage5_First_PersonalTask.entity.Videos;
import com.HNG.Stage5_First_PersonalTask.model.VideoResponseDto;
import com.HNG.Stage5_First_PersonalTask.repository.VideoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoServices{
private final VideoRepo videoRepo;
    private String videoDirectory="/video";
    @Override
    public String startRecording() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String uploadingVideo(MultipartFile videoChunk, String sessionId) throws IOException {

        String videoSessionDirectory = videoDirectory + File.separator + sessionId;
        File videoSessionFile = new File(videoSessionDirectory);

        if (!videoSessionFile.exists()) {
            videoSessionFile.mkdirs(); // Create the directory if it doesn't exist
        }
        if (!videoChunk.isEmpty()) {

            if (videoChunk.getOriginalFilename().equalsIgnoreCase("END_OF_VIDEO")) {
             String filePath= assembleVideoChunks(sessionId);
             saveVideo(filePath,videoChunk.getOriginalFilename(),sessionId);
            }
            String videoChunkFileName = videoSessionDirectory + File.separator + videoChunk.getOriginalFilename();
            File videoChunkFile= new File(videoChunkFileName);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(videoChunkFile))) {
                byte[] bytes = videoChunk.getBytes();
                stream.write(bytes);
                return " video uploading";
            } catch (IOException e) {

            }
        }
        return null;
    }

    private void saveVideo(String file, String name, String sessionId) {
        videoRepo.save(Videos.builder()
                        .name(name)
                        .videoPath(file)
                .build());

    }

    private String assembleVideoChunks(String sessionId) {
        String finalVideoPath="";
        try {
            File[] chunkFiles = new File(videoDirectory).listFiles((dir, name) -> name.startsWith(sessionId + "-"));
            if (chunkFiles != null && chunkFiles.length > 0) {
                File outputFile = new File(videoDirectory + File.separator + sessionId + ".mp4");
                finalVideoPath=outputFile.getPath();
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    for (File chunkFile : chunkFiles) {
                        byte[] chunkData = Files.readAllBytes(chunkFile.toPath());
                        outputStream.write(chunkData);
                    }
                }
                // Delete the temporary chunk files
                for (File chunkFile : chunkFiles) {
                    chunkFile.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalVideoPath;
    }

    @Override
    public Resource playVideo(String videoName) throws IOException {
        Videos videos= videoRepo.findByName(videoName);

        String filePath = videos.getVideoPath();
        Resource videoResource = new FileSystemResource(filePath);

        if (videoResource.exists() && videoResource.isReadable()) {
            return videoResource;
        }
        throw new RuntimeException();
    }

    @Override
    public List<VideoResponseDto> listOfVideo() {
        List<Videos> videosList= videoRepo.findAll();
        return videosList.stream()
                .map(eachVideo->VideoResponseDto.builder()
                        .name(eachVideo.getName())
                        .videoPath(eachVideo.getVideoPath())
                        .build())
                .toList();
    }
}
