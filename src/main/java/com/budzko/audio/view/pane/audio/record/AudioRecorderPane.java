package com.budzko.audio.view.pane.audio.record;

import com.budzko.audio.audio.record.AudioRecorder;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.budzko.audio.view.utils.ViewUtils.addElement;

@Component
@Slf4j
@RequiredArgsConstructor
public class AudioRecorderPane extends Pane {

    private final AudioRecorder audioRecorder;

    @PostConstruct
    public void init() {
        VBox vBox = new VBox(10);
        addElement(this, vBox);

        createStartButton(vBox);
        createStopButton(vBox);
    }

    private void createStartButton(VBox vBox) {
        Button addWordButton = new Button("Start");
        addWordButton.setOnMouseClicked(
                event -> {
                    log.info("Start button was clicked");
                    try {
                        audioRecorder.startRecording();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
        addElement(vBox, addWordButton);
    }

    private void createStopButton(VBox vBox) {
        Button addWordButton = new Button("Stop");
        addWordButton.setOnMouseClicked(
                event -> {
                    log.info("Stop button was clicked");
                    audioRecorder.stopRecording();
                });
        addElement(vBox, addWordButton);
    }
}
