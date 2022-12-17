package com.budzko.audio.view.pane.audio.play;

import com.budzko.audio.audio.play.AudioPayer;
import com.budzko.audio.audio.storage.RecordStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayController {
    private final AudioPayer audioPayer;
    private final PlayState playState;
    private final RecordStorage recordStorage;

    public synchronized void onSelected(String recordName) {
        if (audioPayer.isRun()) {
            stop();
            if (!recordName.equals(playState.getSelectedRecordName())) {
                play(recordName);
            }
        } else {
            play(recordName);
        }
    }

    private void play(String recordName) {
        try {
            AudioInputStream audioInputStream = recordStorage.getRecord(recordName);
            audioPayer.play(audioInputStream);
            playState.setSelectedRecordName(recordName);
        } catch (UnsupportedAudioFileException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void stop() {
        audioPayer.stop();
    }
}
