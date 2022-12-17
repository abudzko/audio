package com.budzko.audio.view.pane;

import com.budzko.audio.config.AppConfig;
import com.budzko.audio.view.pane.audio.play.AudioPlayerPane;
import com.budzko.audio.view.pane.audio.record.AudioRecorderPane;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.budzko.audio.view.utils.ViewUtils.addElement;

@Component
@RequiredArgsConstructor
public class MainPane extends Pane {

    private final Stage stage;
    private final AudioRecorderPane audioRecorderPane;
    private final AudioPlayerPane audioPlayerPane;
    private final AppConfig appConfig;

    @PostConstruct
    void init() {
        Scene scene = new Scene(
                this,
                appConfig.getSize().getWidth(),
                appConfig.getSize().getHeight()
        );
        stage.setScene(scene);
        VBox vBox = new VBox(10);
        addElement(this, vBox);
        addElement(vBox, audioRecorderPane);
        addElement(vBox, audioPlayerPane);
    }
}
