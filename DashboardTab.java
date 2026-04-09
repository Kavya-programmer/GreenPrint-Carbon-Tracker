
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class DashboardTab {

    private FootprintTracker tracker;
    private FlowPane pane = new FlowPane();
    private Label summary = new Label();
    private VBox details = new VBox();

    public VBox root;

    public DashboardTab(FootprintTracker tracker) {
        this.tracker = tracker;
        buildLayout();
        refresh();
    }

    private void buildLayout() {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10));

        root = new VBox(10, summary, pane, details);
        root.setPadding(new Insets(10));
    }

    public void refresh() {
        pane.getChildren().clear();
        details.getChildren().clear();

        ArrayList<EmissionSource> list = tracker.getEntries();

        summary.setText(
            "Entries: " + list.size() +
            " | Total CO2: " + String.format("%.2f", tracker.getTotalEmissions()) + " kg" +
            " | Top User: " + tracker.getUserWithHighestFootprint()
        );

        for (EmissionSource e : list) {

            Label box = new Label(e.getSourceID());
            box.setMinSize(80, 50);
            box.setAlignment(Pos.CENTER);

            double value = e.calculateEmission();

            if (value < 1.0) {
                box.setStyle("-fx-background-color: #c8e6c9;");
            } else if (value <= 3.0) {
                box.setStyle("-fx-background-color: #fff9c4;");
            } else {
                box.setStyle("-fx-background-color: #ffcdd2;");
            }

            final EmissionSource current = e;
            box.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    details.getChildren().clear();
                    details.getChildren().add(new Label(current.toString()));
                }
            });

            pane.getChildren().add(box);
        }
    }
}
