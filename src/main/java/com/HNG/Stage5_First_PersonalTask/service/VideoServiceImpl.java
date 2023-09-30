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
    private String videoDirectory="";
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

            if (videoChunk.getOriginalFilename().equals("END_OF_VIDEO")) {
             String file= assembleVideoChunks(sessionId);
             saveVideo(file,videoChunk.getName(),sessionId);
            }
            String videoChunkFileName = videoSessionDirectory + File.separator + videoChunk.getOriginalFilename();
            File videoChunkFile= new File(videoChunkFileName);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(videoChunkFile))) {
                byte[] bytes = videoChunk.getBytes();
                stream.write(bytes);
                return null;
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
        try {
            File[] chunkFiles = new File(videoDirectory).listFiles((dir, name) -> name.startsWith(sessionId + "-"));
            if (chunkFiles != null && chunkFiles.length > 0) {
                File outputFile = new File(videoDirectory + File.separator + sessionId + ".mp4");
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
        return null;
    }

    @Override
    public ResponseEntity<Resource> playVideo(String videoName) throws IOException {
        Videos videos= videoRepo.findByName(videoName);

        String filePath = videos.getVideoPath();
        Resource videoResource = new FileSystemResource(filePath);

        if (videoResource.exists() && videoResource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(videoResource.contentLength());
            headers.setContentDispositionFormData("attachment", videoName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(videoResource);
        } else {
            return ResponseEntity.notFound().build();
        }
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
