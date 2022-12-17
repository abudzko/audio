package com.budzko.audio.view;

import com.budzko.audio.AppLauncher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApp extends Application {

    private static final String APP_TITLE = "Audio";
    private static final String STAGE_SPRING_BEAN_NAME = "stage";

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        super.init();
    }

    /**
     * After JavaFx Toolkit was initialized then launch SpringBoot context
     */
    @Override
    public void start(Stage stage) {
        applicationContext = new SpringApplicationBuilder(AppLauncher.class)
                .web(WebApplicationType.NONE)
                .initializers(
                        (ApplicationContextInitializer<GenericApplicationContext>) context ->
                                context.registerBean(
                                        STAGE_SPRING_BEAN_NAME,
                                        Stage.class,
                                        () -> stage
                                )
                )
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run();
        configureStage(stage);
        stage.show();
    }

    private void configureStage(Stage stage) {
        stage.setTitle(APP_TITLE);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
        Platform.exit();
    }
}

