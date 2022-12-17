package com.budzko.audio.view.pane.audio.play;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PlayState {
    private String selectedRecordName;
}
