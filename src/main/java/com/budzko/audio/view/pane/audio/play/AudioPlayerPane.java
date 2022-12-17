package com.budzko.audio.view.pane.audio.play;

import com.budzko.audio.audio.storage.AudioRecordMetaData;
import com.budzko.audio.audio.storage.RecordStorage;
import com.budzko.audio.view.pane.audio.record.RecordTableRowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static com.budzko.audio.view.utils.ViewUtils.addElement;

@Component
@Slf4j
@RequiredArgsConstructor
public class AudioPlayerPane extends Pane {
    private final RecordStorage recordStorage;
    private final PlayController playController;

    private final ObservableList<RecordTableRowView> tableElements = FXCollections.observableArrayList();

    @PostConstruct
    public void init() {
        VBox vBox = new VBox(10);
        addElement(this, vBox);

        createRecordTable(vBox);
        createRefreshButton(vBox);
    }

    private void createRecordTable(VBox vBox) {
        TableView<RecordTableRowView> recordTableView = new TableView<>();
        configureRecordTableView(recordTableView);

        refreshTableElements();
        recordTableView.setItems(tableElements);
        addElement(vBox, recordTableView);
    }

    private void refreshTableElements() {
        tableElements.clear();
        List<AudioRecordMetaData> records = recordStorage.getRecords();
        records.sort((record1, record2) -> record2.getDate().compareTo(record1.getDate()));
        for (int i = 0; i < records.size(); i++) {
            AudioRecordMetaData recordMetaData = records.get(i);
            RecordTableRowView recordTableRowView = RecordTableRowView.builder()
                    .number(String.valueOf(i))
                    .name(recordMetaData.getName())
                    .date(recordMetaData.getDate().toString())
                    .sizeBytes(String.valueOf(recordMetaData.getSizeBytes()))
                    .build();
            tableElements.add(recordTableRowView);
        }
    }

    private void configureRecordTableView(TableView<RecordTableRowView> recordTableView) {
        recordTableView.setPrefWidth(700);
        recordTableView.getColumns().addAll(createColumns());
        recordTableView.setOnMousePressed(event -> {
            if (isDoubleMouseClick(event)) {
                RecordTableRowView recordTableRowView = recordTableView.getSelectionModel().getSelectedItem();
                String recordName = recordTableRowView.getName();
                playController.onSelected(recordName);
            }
        });
    }

    private boolean isDoubleMouseClick(MouseEvent event) {
        return event.isPrimaryButtonDown() && event.getClickCount() == 2;
    }

    private List<TableColumn<RecordTableRowView, ?>> createColumns() {
        return List.of(
                new Pair<>("number", "Number"),
                new Pair<>("name", "Name"),
                new Pair<>("sizeBytes", "Size, bytes"),
                new Pair<>("date", "Date")
        ).stream()
                .map(meta ->
                        {
                            TableColumn<RecordTableRowView, String> column = new TableColumn<>(meta.getValue());
                            column.setCellValueFactory(new PropertyValueFactory<>(meta.getKey()));
                            return column;
                        }
                ).collect(Collectors.toList());
    }

    private void createRefreshButton(VBox vBox) {
        Button addWordButton = new Button("Refresh");
        addWordButton.setOnMouseClicked(
                event -> {
                    log.info("Refresh button was clicked");
                    refreshTableElements();
                });
        addElement(vBox, addWordButton);
    }
}
