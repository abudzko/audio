package com.budzko.audio.view.pane.audio.record;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecordTableRowView {
    private String number;
    private String name;
    private String date;
    private String sizeBytes;
}
