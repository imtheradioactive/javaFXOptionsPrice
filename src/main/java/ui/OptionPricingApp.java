package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.MonteCarloPricer;
import utils.OptionParameters;

public class OptionPricingApp {
    private TextField strikeField;
    private TextField maturityField;
    private TextField interestRateField;
    private TextField meanReversionRateField;
    private TextField longTermVarianceField;
    private TextField volatilityOfVarianceField;
    private TextField correlationField;
    private TextField simulationsField;
    private TextField threadPoolField;
    private TextArea bermudanDatesField;
    private ComboBox<String> optionTypeBox;
    private ComboBox<String> optionPriceTypeBox;
    private Button calculateButton;
    private Label resultLabel;

    public Scene getMainScene(Stage primaryStage) {
        optionTypeBox = new ComboBox<>();
        optionTypeBox.getItems().addAll("European", "American", "Bermudan");
        optionTypeBox.setValue("European");

        optionPriceTypeBox = new ComboBox<>();
        optionPriceTypeBox.getItems().addAll("Call", "Put");
        optionPriceTypeBox.setValue("Call");

        strikeField = new TextField();
        maturityField = new TextField();
        interestRateField = new TextField();
        meanReversionRateField = new TextField();
        longTermVarianceField = new TextField();
        volatilityOfVarianceField = new TextField();
        correlationField = new TextField();
        simulationsField = new TextField("10000");
        threadPoolField = new TextField("10");

        bermudanDatesField = new TextArea();
        bermudanDatesField.setPromptText("Enter exercise dates separated by commas (e.g., 0.5, 1.0, 1.5)");

        Label optionTypeLabel = new Label("Option Type:");
        Label optionPriceTypeLabel = new Label("Price Type:");
        Label strikeLabel = new Label("Strike Price:");
        Label maturityLabel = new Label("Maturity (years):");
        Label interestRateLabel = new Label("Interest Rate:");
        Label meanReversionRateLabel = new Label("Mean Reversion Rate (kappa):");
        Label longTermVarianceLabel = new Label("Long-Term Variance (theta):");
        Label volatilityOfVarianceLabel = new Label("Volatility of Variance (sigma):");
        Label correlationLabel = new Label("Correlation (rho):");
        Label simulationsLabel = new Label("Monte Carlo Simulations:");
        Label threadPoolLabel = new Label("Thread Pool Size:");
        Label bermudanDatesLabel = new Label("Bermudan Exercise Dates:");

        calculateButton = new Button("Calculate Price");
        resultLabel = new Label();

        calculateButton.setOnAction(event -> {
            try {
                String optionType = optionTypeBox.getValue();
                String priceType = optionPriceTypeBox.getValue();
                double strikePrice = Double.parseDouble(strikeField.getText());
                double maturity = Double.parseDouble(maturityField.getText());
                double interestRate = Double.parseDouble(interestRateField.getText());
                double kappa = Double.parseDouble(meanReversionRateField.getText());
                double theta = Double.parseDouble(longTermVarianceField.getText());
                double sigma = Double.parseDouble(volatilityOfVarianceField.getText());
                double rho = Double.parseDouble(correlationField.getText());
                int numSimulations = Integer.parseInt(simulationsField.getText());
                int threadPoolSize = Integer.parseInt(threadPoolField.getText());

                double[] bermudanDates = null;
                if ("Bermudan".equalsIgnoreCase(optionType)) {
                    String[] datesStr = bermudanDatesField.getText().split(",");
                    bermudanDates = new double[datesStr.length];
                    for (int i = 0; i < datesStr.length; i++) {
                        bermudanDates[i] = Double.parseDouble(datesStr[i].trim());
                    }
                }

                OptionParameters params = new OptionParameters(optionType, strikePrice, maturity, interestRate,
                        kappa, theta, sigma, rho, numSimulations, threadPoolSize, bermudanDates);

                MonteCarloPricer pricer = new MonteCarloPricer(params);
                resultLabel.setText("Calculating, please wait...");
                calculateButton.setDisable(true);

                new Thread(() -> {
                    double optionPrice = priceType.equals("Call") ? pricer.call() : pricer.put();
                    Platform.runLater(() -> {
                        resultLabel.setText("Calculated Option Price: " + optionPrice);
                        calculateButton.setDisable(false);
                    });
                }).start();

            } catch (NumberFormatException ex) {
                showError("Invalid input. Please ensure all fields are filled correctly.");
            } catch (Exception ex) {
                showError("An error occurred during calculation: " + ex.getMessage());
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(optionTypeLabel, 0, 0);
        gridPane.add(optionTypeBox, 1, 0);
        gridPane.add(optionPriceTypeLabel, 0, 1);
        gridPane.add(optionPriceTypeBox, 1, 1);
        gridPane.add(strikeLabel, 0, 2);
        gridPane.add(strikeField, 1, 2);
        gridPane.add(maturityLabel, 0, 3);
        gridPane.add(maturityField, 1, 3);
        gridPane.add(interestRateLabel, 0, 4);
        gridPane.add(interestRateField, 1, 4);
        gridPane.add(meanReversionRateLabel, 0, 5);
        gridPane.add(meanReversionRateField, 1, 5);
        gridPane.add(longTermVarianceLabel, 0, 6);
        gridPane.add(longTermVarianceField, 1, 6);
        gridPane.add(volatilityOfVarianceLabel, 0, 7);
        gridPane.add(volatilityOfVarianceField, 1, 7);
        gridPane.add(correlationLabel, 0, 8);
        gridPane.add(correlationField, 1, 8);
        gridPane.add(simulationsLabel, 0, 9);
        gridPane.add(simulationsField, 1, 9);
        gridPane.add(threadPoolLabel, 0, 10);
        gridPane.add(threadPoolField, 1, 10);
        gridPane.add(bermudanDatesLabel, 0, 11);
        gridPane.add(bermudanDatesField, 1, 11);
        gridPane.add(calculateButton, 1, 12);
        gridPane.add(resultLabel, 1, 13);

        VBox vbox = new VBox(10, gridPane);
        vbox.setPadding(new Insets(10));
        return new Scene(vbox, 500, 750);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
