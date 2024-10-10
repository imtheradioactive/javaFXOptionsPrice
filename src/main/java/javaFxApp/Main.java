package javaFxApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.OptionPricingApp;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        OptionPricingApp optionPricingApp = new OptionPricingApp();
        Scene scene = optionPricingApp.getMainScene(primaryStage);
        primaryStage.setTitle("Option Pricing Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
