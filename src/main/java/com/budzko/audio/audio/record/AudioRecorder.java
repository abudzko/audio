package com.budzko.audio.audio.record;

import com.budzko.audio.audio.storage.RecordStorage;
import com.budzko.audio.config.AudioConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.ByteArrayOutputStream;


/**
 * Records audio from microphone
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AudioRecorder {

    private final AudioConfig audioConfig;
    private final RecordStorage recordStorage;

    private volatile boolean isRun = false;
    private Thread recordReaderThread;
    private ByteArrayOutputStream recordBuffer = new ByteArrayOutputStream(0);

    public synchronized void startRecording() {
        if (isRun) {
            log.info("Recording already was started");
        } else {
            isRun = true;
            recordBuffer = new ByteArrayOutputStream();
            recordReaderThread = new Thread(() -> {
                try {
                    readAudioInputStream();
                } catch (LineUnavailableException e) {
                    log.error(e.getMessage(), e);
                }
            });
            recordReaderThread.start();
        }
    }

    private void readAudioInputStream() throws LineUnavailableException {
        byte[] buffer = new byte[100];
        final TargetDataLine line = createTargetDataLine(audioConfig.getAudioFormat());
        while (this.isRun) {
            int readBytesCount = line.read(buffer, 0, buffer.length);
            if (readBytesCount == -1) {
                this.isRun = false;
                break;
            }
            recordBuffer.write(buffer, 0, readBytesCount);
        }
    }

    public synchronized void stopRecording() {
        if (isRun) {
            isRun = false;
            try {
                recordReaderThread.join();
            } catch (InterruptedException e) {
                recordReaderThread.interrupt();
                log.error(e.getMessage(), e);
            }
            if (recordBuffer.size() > 0) {
                recordStorage.saveRecord(recordBuffer);
                recordBuffer.reset();
            }
        } else {
            log.info("Recording was not started");
        }
    }

    private TargetDataLine createTargetDataLine(AudioFormat format) throws LineUnavailableException {
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new IllegalStateException("Line is not supported:" + info + " " + format);
        }

        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format, line.getBufferSize());
        line.start();
        return line;
    }
}
