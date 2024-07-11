package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StockManagementSimulator extends Application {

    @Override
    public void start(Stage primaryStage) {
    primaryStage.setTitle("Simulador de Gestión de Stock");

    Label label = new Label("Simulación de Gestión de Stock");
    label.setAlignment(Pos.CENTER); // Alinea el texto del label al centro
    label.setMaxWidth(Double.MAX_VALUE); // Permite que el label se expanda para centrarse correctamente

    Button buttonA = new Button("Simular Política A");
    Button buttonB = new Button("Simular Política B");

    buttonA.setOnAction(e -> simulatePolicyA());
    buttonB.setOnAction(e -> simulatePolicyB());

    VBox vbox = new VBox(buttonA, buttonB);
    vbox.setSpacing(10);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(label); // Coloca el label en la parte superior del BorderPane
    BorderPane.setAlignment(label, Pos.CENTER); // Centra el label dentro de la región superior
    borderPane.setCenter(vbox); // Coloca el VBox en el centro

    Scene scene = new Scene(borderPane, 300, 200);

    primaryStage.setScene(scene);
    primaryStage.show();
}

    private void simulatePolicyA() {
        // Lógica para simular la Política A
    }

    private void simulatePolicyB() {
        // Lógica para simular la Política B
    }

    public static void main(String[] args) {
        launch(); //usar mvn clean javafx:run
    }
}

