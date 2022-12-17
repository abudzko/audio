package com.budzko.audio.audio.storage;

import com.budzko.audio.config.AudioConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves file on the disk
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RecordStorage {

    private final AudioConfig audioConfig;

    public void saveRecord(ByteArrayOutputStream outputStream) {
        AudioFormat format = audioConfig.getAudioFormat();
        byte[] audioBytes = outputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioStream = new AudioInputStream(
                byteArrayInputStream,
                format,
                audioBytes.length / format.getFrameSize()
        );
        try {
            String fileName = buildResultFileName();
            AudioSystem.write(
                    audioStream,
                    audioConfig.getAudioFileType(),
                    audioConfig.getAudioRecordsLocationPath().resolve(fileName).toFile()
            );
            log.info("File [{}] of size {} bytes was stored", fileName, audioBytes.length);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String buildResultFileName() {
        return System.currentTimeMillis() + "." + audioConfig.getAudioFileType().getExtension();
    }

    public List<AudioRecordMetaData> getRecords() {
        File recordLocation = audioConfig.getAudioRecordsLocationPath().toFile();
        ArrayList<AudioRecordMetaData> audioRecordMetaData = new ArrayList<>();
        if (recordLocation.exists()) {
            File[] records = recordLocation.listFiles();
            if (records != null) {
                for (File file : records) {
                    String name = file.getName();
                    long length = file.length();
                    long lastModified = file.lastModified();
                    LocalDateTime date =
                            Instant.ofEpochMilli(lastModified).atZone(ZoneId.of("UTC")).toLocalDateTime();
                    AudioRecordMetaData recordMetaData = AudioRecordMetaData.builder()
                            .name(name)
                            .sizeBytes(length)
                            .date(date)
                            .build();
                    audioRecordMetaData.add(recordMetaData);
                }
            } else {
                log.info("Can't find no one record");
            }
        }
        return audioRecordMetaData;
    }

    public AudioInputStream getRecord(String fileName) throws UnsupportedAudioFileException, IOException {
        return AudioSystem.getAudioInputStream(audioConfig.getAudioRecordsLocationPath().resolve(fileName).toFile());
    }
}
