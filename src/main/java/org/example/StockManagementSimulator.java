package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class StockManagementSimulator extends Application {

    // Datos de entrada
    static Integer cantidadDiasSimulacion = null; //~ cantidad de días a simular
    static Integer stockInicial = null; //~ stock inicial
    static Integer diasEntrePedidos = null; //~ cada cuántos días se pide
    static Integer cantidadAPedir = null; //~ tamaño del pedido

    @Override
    public void start(Stage primaryStage) {
    primaryStage.setTitle("Simulador de Gestión de Stock");

    Label titulo = new Label("Simulación de Gestión de Stock");
    titulo.setAlignment(Pos.CENTER); // Alinea el texto del label al centro
    titulo.setMaxWidth(Double.MAX_VALUE); // Permite que el label se expanda para centrarse correctamente

    // X=N Cantidad de días = Cantidad de filas generadas. Debe soportar hasta 100000.
    Label cantidadDiasSimulacionLabel = new Label("Ingrese la cantidad de días a simular (entre 2 y 10^8:)");
    TextField cantidadDiasSimulacionInput = new TextField();
    cantidadDiasSimulacionInput.setPromptText("Ej.: 300");
    cantidadDiasSimulacionInput.setText(cantidadDiasSimulacion == null ? "" : Integer.toString(cantidadDiasSimulacion));
    cantidadDiasSimulacionInput.textProperty().addListener((observable, oldValue, newValue) -> cantidadDiasSimulacion = Integer.parseInt(newValue)); // Actualiza la variable cantidadDiasSimulacion

    // Stock inicial = X0
    Label stockInicialLabel = new Label("Ingrese el stock inicial:");
    TextField stockInicialInput = new TextField();
    stockInicialInput.setPromptText("Ej.: 20");
    stockInicialInput.setText(stockInicial == null ? "" : Integer.toString(stockInicial));
    stockInicialInput.textProperty().addListener((observable, oldValue, newValue) -> stockInicial = Integer.parseInt(newValue)); // Actualiza la variable stockInicial

    // Dias entre pedidos = N
    Label diasEntrePedidosLabel = new Label("Ingrese la cantidad de días entre pedidos:");
    TextField diasEntrePedidosInput = new TextField();
    diasEntrePedidosInput.setPromptText("Ej.: 10");
    diasEntrePedidosInput.setText(diasEntrePedidos == null ? "" : Integer.toString(diasEntrePedidos));
    diasEntrePedidosInput.textProperty().addListener((observable, oldValue, newValue) -> diasEntrePedidos = Integer.parseInt(newValue)); // Actualiza la variable diasEntrePedidos

    // Cantidad a pedir = Q
    Label cantidadAPedirLabel = new Label("Ingrese la cantidad a pedir:");
    TextField cantidadAPedirInput = new TextField();
    cantidadAPedirInput.setPromptText("Ej.: 100");
    cantidadAPedirInput.setText(cantidadAPedir == null ? "" : Integer.toString(cantidadAPedir));
    cantidadAPedirInput.textProperty().addListener((observable, oldValue, newValue) -> cantidadAPedir = Integer.parseInt(newValue)); // Actualiza la variable cantidadAPedir

    Button buttonA = new Button("Simular Política A");
    Button buttonB = new Button("Simular Política B");

    buttonA.setOnAction(e -> simulatePolicyA());
    buttonB.setOnAction(e -> simulatePolicyB());

    VBox vbox = new VBox(titulo, cantidadDiasSimulacionLabel, cantidadDiasSimulacionInput, stockInicialLabel, stockInicialInput, diasEntrePedidosLabel, diasEntrePedidosInput, cantidadAPedirLabel, cantidadAPedirInput, buttonA, buttonB);
    vbox.setSpacing(10);
    vbox.setAlignment(Pos.CENTER); // Centra el VBox
    vbox.setPadding(new Insets(50));



    Scene scene = new Scene(vbox, 640, 480);

    primaryStage.setScene(scene);
    primaryStage.show();
    }


    private void simulatePolicyA() {
        // Datos del vector de estado
        double[][] matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "A");

        // Nombre del archivo Excel a crear
        String nombreArchivo = "SimulacionPoliticaA.xls";

        // Llamar al método crearExcel de GeneradorExcel
        GeneradorExcel.crearExcel(nombreArchivo, matriz);

    }

    private void simulatePolicyB() {
        // Datos del vector de estado
        double[][] matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "B");

        // Nombre del archivo Excel a crear
        String nombreArchivo = "SimulacionPoliticaB.xls";

        // Llamar al método crearExcel de GeneradorExcel
        GeneradorExcel.crearExcel(nombreArchivo, matriz);
    }

    public static void main(String[] args) {
        launch(); //usar mvn clean javafx:run o mvn javafx:run
    }
}

