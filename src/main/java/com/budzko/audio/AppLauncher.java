package com.budzko.audio;

import com.budzko.audio.view.JavaFxApp;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppLauncher {
    public static void main(String[] args) {
        //First launch JavaFx Toolkit
        Application.launch(JavaFxApp.class, args);
    }
}
