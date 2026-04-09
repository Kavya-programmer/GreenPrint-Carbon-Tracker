
/*
Main entry point of the GreenPrint GUI application.
Extends the JavaFX Application class and is responsible for creating
the shared FootprintTracker, building the three tabs, and displaying the window.
All tabs share the same tracker instance so they always work with the same data.

On startup, previously saved entries are restored from greenprint_state.txt.
On close, all current entries are saved back to greenprint_state.txt.
*/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GreenPrintGUI extends Application {

    // Shared tracker used by all three tabs
    private FootprintTracker tracker = new FootprintTracker();

    /*
    Called automatically by JavaFX when the application starts.
    Loads saved state first, then creates the three tab panels,
    assembles them into a TabPane, and configures the main window.
    Saves state automatically when the window is closed.

    stage: the primary window provided by JavaFX
    */
    @Override
    public void start(Stage stage) throws Exception {

        // Load any previously saved entries before showing the window
        StateManager.loadState();

        // Create the three tab panels
        DashboardTab dashboardTab = new DashboardTab(tracker);
        InputTab inputTab = new InputTab(tracker, dashboardTab);
        OffsetTab offsetTab = new OffsetTab(tracker);

        // Wrap each panel in a Tab object
        // DashboardTab and OffsetTab use .root
        // InputTab uses .getContent()
        Tab tab1 = new Tab("Live Dashboard", dashboardTab.root);
        Tab tab2 = new Tab("Input & Operations", inputTab.getContent());
        Tab tab3 = new Tab("Carbon Offset", offsetTab.root);

        // Remove the close button from each tab
        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);

        // Assemble TabPane
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(tab1, tab2, tab3);

        // Create and show the window
        Scene scene = new Scene(tabPane, 900, 600);
        stage.setScene(scene);
        stage.setTitle("GreenPrint Carbon Tracker");
        stage.setMinHeight(400);
        stage.setMinWidth(600);

        /*
        When the user closes the window, save all entries to file
        and log the event. */
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                StateManager.saveState(tracker);
                GreenPrintLogger.logOperations("STATE_SAVED", "Application closed, state saved to greenprint_state.txt");
            }
        });

        stage.show();
        GreenPrintLogger.logOperations("APP_STARTED", "GreenPrint launched");
    }

    /*
    Standard Java main method. Calls launch() to start the JavaFX application.
    */
    public static void main(String[] args) {
        launch(args);
    }
}
