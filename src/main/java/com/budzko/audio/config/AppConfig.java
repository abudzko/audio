package com.budzko.audio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Reads application.yaml
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private AudioConfig audio;
    private AppSize size;

    @Data
    public static class AudioConfig {
        private AudioRecordConfig record;

        @Data
        public static class AudioRecordConfig {

            private String locationPath;
            private String type;
            private AudioFileFormatConfig format;

            @Data
            public static class AudioFileFormatConfig {
                private String encoding;
                private float sampleRate;
                private int channelCount;
                private int sampleSize;
                private boolean bigEndian;
            }
        }
    }

    @Data
    public static class AppSize {
        private int width;
        private int height;
    }
}
