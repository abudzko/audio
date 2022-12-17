package com.budzko.audio.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * TODO: change configuration in runtime
 */
@Component
@RequiredArgsConstructor
public class ConfigChangeListener {
    private final AppConfig appConfig;
    private final AudioConfig audioConfig;

    public void setLocationPath(String locationPath) {
        getAudioFileConfig().setLocationPath(locationPath);
        audioConfig.buildAudioRecordsLocationPath();
    }

    public void setType(String type) {
        getAudioFileConfig().setType(type);
        audioConfig.buildAudioFileType();
    }

    public void setEncoding(String encoding) {
        getAudioFileFormatConfig().setEncoding(encoding);
        audioConfig.buildAudioFormat();
    }

    public void setSampleRate(float sampleRate) {
        getAudioFileFormatConfig().setSampleRate(sampleRate);
        audioConfig.buildAudioFormat();
    }

    public void setChannelCount(int channelCount) {
        getAudioFileFormatConfig().setChannelCount(channelCount);
        audioConfig.buildAudioFormat();
    }

    public void setSampleSize(int sampleSize) {
        getAudioFileFormatConfig().setSampleSize(sampleSize);
        audioConfig.buildAudioFormat();
    }

    public void setBigEndian(boolean bigEndian) {
        getAudioFileFormatConfig().setBigEndian(bigEndian);
        audioConfig.buildAudioFormat();
    }

    private AppConfig.AudioConfig.AudioRecordConfig getAudioFileConfig() {
        return appConfig.getAudio().getRecord();
    }

    private AppConfig.AudioConfig.AudioRecordConfig.AudioFileFormatConfig getAudioFileFormatConfig() {
        return getAudioFileConfig().getFormat();
    }
}
