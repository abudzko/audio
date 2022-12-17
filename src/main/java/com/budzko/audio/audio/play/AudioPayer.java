package com.budzko.audio.audio.play;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;


/**
 * Records audio from microphone
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AudioPayer {
    private Clip clip;

    @Getter
    private volatile boolean isRun = false;

    public synchronized void play(AudioInputStream audioInputStream) {
        play(audioInputStream, 0);
    }

    public synchronized void play(AudioInputStream audioInputStream, int framePosition) {
        if (isRun) {
            log.info("Playback is on already");
        } else {
            isRun = true;
            Thread recordPlayerThread = new Thread(() -> {
                try {
                    clip = AudioSystem.getClip();
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            stop();
                        }
                    });
                    clip.open(audioInputStream);
                    clip.start();
                    clip.setFramePosition(framePosition);
                } catch (LineUnavailableException | IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
            recordPlayerThread.start();
        }
    }

    public synchronized int stop() {
        if (isRun) {
            isRun = false;
            clip.stop();
            return clip.getFramePosition();
        }
        return clip.getFrameLength();
    }
}
