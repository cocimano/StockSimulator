package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    Label cantidadDiasSimulacionLabel = new Label("Ingrese la cantidad de días a simular");
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
    Button buttonLimpiar = new Button("Limpiar");
    Button buttonSalir = new Button("Salir");
    Button buttonEvaluar = new Button("Evaluar");

    buttonA.setOnAction(e -> {
        if (validarEntradaNumerica(cantidadDiasSimulacionInput, "Cantidad de días a simular") &&
            validarEntradaNumerica(stockInicialInput, "Stock inicial") &&
            validarEntradaNumerica(diasEntrePedidosInput, "Días entre pedidos") &&
            validarEntradaNumerica(cantidadAPedirInput, "Cantidad a pedir")) {
            simulatePolicyA(); // Proceder solo si todas las validaciones son exitosas
        }
    });

    buttonB.setOnAction(e -> {
        if (validarEntradaNumerica(cantidadDiasSimulacionInput, "Cantidad de días a simular") &&
            validarEntradaNumerica(stockInicialInput, "Stock inicial") &&
            validarEntradaNumerica(diasEntrePedidosInput, "Días entre pedidos") &&
            validarEntradaNumerica(cantidadAPedirInput, "Cantidad a pedir")) {
            simulatePolicyB(); // Proceder solo si todas las validaciones son exitosas
        }
    });

    buttonLimpiar.setOnAction(e -> {
        cantidadDiasSimulacion = null;
        stockInicial = null;
        diasEntrePedidos = null;
        cantidadAPedir = null;
        cantidadDiasSimulacionInput.clear();
        stockInicialInput.clear();
        diasEntrePedidosInput.clear();
        cantidadAPedirInput.clear();
    });

    buttonSalir.setOnAction(e -> {
        primaryStage.close();
    });

    buttonEvaluar.setOnAction(e -> {
        if (costoPromedio[0] == 0 || costoPromedio[1] == 0) {
            mostrarAlerta("Error", "Primero debe simular las políticas A y B." + costoPromedio[0] + " " + costoPromedio[1]);
        } else {
            if (costoPromedio[0] < costoPromedio[1]) {
                mostrarAlerta("Resultado", "La política A es más económica que la B. \nCosto promedio A: $" + costoPromedio[0] + ", Costo promedio B: $" + costoPromedio[1]);
            } else if (costoPromedio[0] > costoPromedio[1]) {
                mostrarAlerta("Resultado", "La política B es más económica que la A. \nCosto promedio B: $" + costoPromedio[1] + ", Costo promedio A: $" + costoPromedio[0]);
            } else {
                mostrarAlerta("Resultado", "Ambas políticas tienen el mismo costo promedio. \nCosto promedio: $" + costoPromedio[0]);
            }
        }
    });

    VBox vbox = new VBox(titulo, cantidadDiasSimulacionLabel, cantidadDiasSimulacionInput, stockInicialLabel, stockInicialInput, diasEntrePedidosLabel, diasEntrePedidosInput, cantidadAPedirLabel, cantidadAPedirInput);
    vbox.setSpacing(10);
    vbox.setAlignment(Pos.CENTER); // Centra el VBox
    vbox.setPadding(new Insets(50));

    // Usar HBox para los botones de las políticas
    HBox hboxButtons = new HBox(buttonA, buttonB);
    hboxButtons.setSpacing(10); // Espacio entre los botones
    hboxButtons.setAlignment(Pos.CENTER); // Centra los botones horizontalmente

    // Usar HBox para los botones de limpiar y salir
    HBox hboxButtons2 = new HBox(buttonLimpiar, buttonSalir);
    hboxButtons2.setSpacing(10); // Espacio entre los botones
    hboxButtons2.setAlignment(Pos.CENTER); // Centra los botones horizontalmente

    // Agregar los HBox de botones al VBox principal
    vbox.getChildren().add(hboxButtons);
    vbox.getChildren().add(hboxButtons2);

    // Agregar botón de evaluación
    vbox.getChildren().add(buttonEvaluar);

    Scene scene = new Scene(vbox, 640, 480);

    primaryStage.setScene(scene);
    primaryStage.show();
    }

    // Validar que los datos de entrada sean numeros, no nulos, y que sean mayores a 0
    private boolean validarEntradaNumerica(TextField campo, String nombreCampo) {
        if (campo.getText() == null || campo.getText().isEmpty()) {
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' no puede ser nulo o estar vacío.");
            return false;
        }
        try {
            int valor = Integer.parseInt(campo.getText());
            if (valor <= 0) {
                throw new IllegalArgumentException("El campo '" + nombreCampo + "' debe ser mayor a 0.");
            }
            return true; // Validación exitosa
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' debe ser numérico.");
            return false;
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de validación", e.getMessage());
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    // Array para guardar el costoPromedio de la simulación
    private double[] costoPromedio = new double[2];


    private void simulatePolicyA() {
        // Datos del vector de estado
        double[][] matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "A");
        costoPromedio[0] = matriz[matriz.length - 1][15]; // Guardar el costo promedio de la simulación

        // Nombre del archivo Excel a crear
        String nombreArchivo = "SimulacionPoliticaA.xls";

        // Llamar al método crearExcel de GeneradorExcel
        GeneradorExcel.crearExcel(nombreArchivo, matriz);

    }

    private void simulatePolicyB() {
        // Datos del vector de estado
        double[][] matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "B");
        costoPromedio[1] = matriz[matriz.length - 1][15]; // Guardar el costo promedio de la simulación

        // Nombre del archivo Excel a crear
        String nombreArchivo = "SimulacionPoliticaB.xls";

        // Llamar al método crearExcel de GeneradorExcel
        GeneradorExcel.crearExcel(nombreArchivo, matriz);
    }

    public static void main(String[] args) {
        launch(); //usar mvn clean javafx:run o mvn javafx:run
    }
}

