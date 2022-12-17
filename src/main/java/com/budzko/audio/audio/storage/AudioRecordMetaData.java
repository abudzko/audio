package com.budzko.audio.audio.storage;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AudioRecordMetaData {
    private String name;
    private long sizeBytes;
    private LocalDateTime date;
}
