package com.budzko.audio.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class AudioConfig {
    private final AppConfig appConfig;

    @Getter
    private AudioFormat audioFormat;
    @Getter
    private AudioFileFormat.Type audioFileType = AudioFileFormat.Type.WAVE;
    @Getter
    private Path audioRecordsLocationPath;

    @PostConstruct
    void init() {
        buildAudioFormat();
        buildAudioFileType();
        buildAudioRecordsLocationPath();
    }

    void buildAudioFormat() {
        var audioFileFormatConfig = appConfig.getAudio()
                .getRecord()
                .getFormat();
        audioFormat = new AudioFormat(
                new AudioFormat.Encoding(audioFileFormatConfig.getEncoding()),
                audioFileFormatConfig.getSampleRate(),
                audioFileFormatConfig.getSampleSize(),
                audioFileFormatConfig.getChannelCount(),
                (audioFileFormatConfig.getSampleSize() / 8) * audioFileFormatConfig.getChannelCount(),
                audioFileFormatConfig.getSampleRate(),
                audioFileFormatConfig.isBigEndian()
        );
    }

    void buildAudioFileType() {
        var audioFileConfig = appConfig.getAudio().getRecord();
        String type = audioFileConfig.getType();
        if (AudioFileFormat.Type.WAVE.getExtension().equals(type)) {
            audioFileType = AudioFileFormat.Type.WAVE;
        } else {
            throw new IllegalArgumentException("Not supported type:" + type);
        }
    }

    void buildAudioRecordsLocationPath() {
        audioRecordsLocationPath = Paths.get(appConfig.getAudio().getRecord().getLocationPath());
    }
}
