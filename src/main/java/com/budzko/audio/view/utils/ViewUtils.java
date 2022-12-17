package com.budzko.audio.view.utils;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ViewUtils {
    public static boolean addElement(Pane parent, Region element) {
        return parent.getChildren().add(element);
    }
}
