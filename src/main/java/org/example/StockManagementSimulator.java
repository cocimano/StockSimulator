package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class StockManagementSimulator extends Application {

    // Datos de entrada
    static Integer cantidadDiasSimulacion = null; //~ cantidad de días a simular
    static Integer stockInicial = null; //~ stock inicial
    static Integer diasEntrePedidos = null; //~ cada cuántos días se pide
    static Integer cantidadAPedir = null; //~ tamaño del pedido

    public static void main(String[] args) {
        launch(); //usar mvn clean javafx:run o mvn javafx:run
        //Application.launch(StockManagementSimulator.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
    primaryStage.setTitle("Simulador de Gestión de Stock");

    Label titulo = new Label("Simulación de Gestión de Stock");
    titulo.setAlignment(Pos.CENTER); // Alinea el texto del label al centro
    titulo.setMaxWidth(Double.MAX_VALUE); // Permite que el label se expanda para centrarse correctamente
    titulo.setFont(new Font("Cambria", 23)); // Cambiar el tamaño de la fuente

    // X=N Cantidad de días = Cantidad de filas generadas. Debe soportar hasta 100000.
    Label cantidadDiasSimulacionLabel = new Label("Ingrese la cantidad de días a simular (hasta 100.000):");
    cantidadDiasSimulacionLabel.setFont(new Font("Cambria", 14)); 
    TextField cantidadDiasSimulacionInput = new TextField();
    cantidadDiasSimulacionInput.setPromptText("Ej.: 300");
    cantidadDiasSimulacionInput.setText(cantidadDiasSimulacion == null ? "" : Integer.toString(cantidadDiasSimulacion));
    cantidadDiasSimulacionInput.textProperty().addListener((observable, oldValue, newValue) -> cantidadDiasSimulacion = Integer.parseInt(newValue)); // Actualiza la variable cantidadDiasSimulacion

    // Stock inicial = X0
    Label stockInicialLabel = new Label("Ingrese el stock inicial:");
    stockInicialLabel.setFont(new Font("Cambria", 14)); 
    TextField stockInicialInput = new TextField();
    stockInicialInput.setPromptText("Ej.: 20");
    stockInicialInput.setText(stockInicial == null ? "" : Integer.toString(stockInicial));
    stockInicialInput.textProperty().addListener((observable, oldValue, newValue) -> stockInicial = Integer.parseInt(newValue)); // Actualiza la variable stockInicial

    // Dias entre pedidos = N
    Label diasEntrePedidosLabel = new Label("Ingrese la cantidad de días entre pedidos:");
    diasEntrePedidosLabel.setFont(new Font("Cambria", 14));
    TextField diasEntrePedidosInput = new TextField();
    diasEntrePedidosInput.setPromptText("Ej.: 10");
    diasEntrePedidosInput.setText(diasEntrePedidos == null ? "" : Integer.toString(diasEntrePedidos));
    diasEntrePedidosInput.textProperty().addListener((observable, oldValue, newValue) -> diasEntrePedidos = Integer.parseInt(newValue)); // Actualiza la variable diasEntrePedidos

    // Cantidad a pedir = Q
    Label cantidadAPedirLabel = new Label("Ingrese la cantidad a pedir:");
    cantidadAPedirLabel.setFont(new Font("Cambria", 14));
    TextField cantidadAPedirInput = new TextField();
    cantidadAPedirInput.setPromptText("Ej.: 15");
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
            simulatePolicy("A"); // Proceder solo si todas las validaciones son exitosas
        }
    });

    buttonB.setOnAction(e -> {
        if (validarEntradaNumerica(cantidadDiasSimulacionInput, "Cantidad de días a simular") &&
            validarEntradaNumerica(stockInicialInput, "Stock inicial") &&
            validarEntradaNumerica(diasEntrePedidosInput, "Días entre pedidos") &&
            validarEntradaNumerica(cantidadAPedirInput, "Cantidad a pedir")) {
            simulatePolicy("B"); // Proceder solo si todas las validaciones son exitosas
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
            mostrarAlerta("Error", "Primero debe simular las políticas A y B. \n" + "A: $" + costoPromedio[0] + " " + "B: $" + costoPromedio[1], "ERROR");
        } else {
            if (costoPromedio[0] < costoPromedio[1]) {
                mostrarAlerta("Resultado", "La política A es más económica que la B. \nCosto promedio A: $" + costoPromedio[0] + ", Costo promedio B: $" + costoPromedio[1], "INFORMACION");
            } else if (costoPromedio[0] > costoPromedio[1]) {
                mostrarAlerta("Resultado", "La política B es más económica que la A. \nCosto promedio B: $" + costoPromedio[1] + ", Costo promedio A: $" + costoPromedio[0], "INFORMACION");
            } else {
                mostrarAlerta("Resultado", "Ambas políticas tienen el mismo costo promedio. \nCosto promedio: $" + costoPromedio[0], "INFORMACION");
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
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' no puede ser nulo o estar vacío.", "ERROR");
            return false;
        }
        try {
            int valor = Integer.parseInt(campo.getText());
            if (valor <= 0) {
                throw new IllegalArgumentException("El campo '" + nombreCampo + "' debe ser mayor a 0.");
            }
            return true; // Validación exitosa
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' debe ser numérico.", "ERROR");
            return false;
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de validación", e.getMessage(), "ERROR");
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, String tipoAlerta) {
        if (tipoAlerta.equals("ERROR")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        } else if (tipoAlerta.equals("INFORMACION")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        }
    }

    private void ventanaTablaCreadaExitosamente() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tabla creada exitosamente");
        alert.setHeaderText(null);
        alert.setContentText("La tabla ha sido creada exitosamente.");
        alert.showAndWait();
    }

    // Método para mostrar la progressBar en una ventana
    Stage progressBarStage = new Stage();

    public void showProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

        Label messageLabel = new Label("Simulación en progreso...");

        StackPane root = new StackPane();
        root.getChildren().addAll(progressBar, messageLabel);

        StackPane.setAlignment(messageLabel, Pos.CENTER);
        StackPane.setMargin(messageLabel, new Insets(50, 0, 0, 0)); // Ajustar según necesidad
        
        Scene scene = new Scene(root, 200, 200);
        progressBarStage.setScene(scene);
        progressBarStage.show();
    }



    private double[] costoPromedio = new double[2]; // Guardar el costo promedio de las simulaciones A y B
    private String nombreArchivo; // Nombre del archivo de Excel generado

    public void simulatePolicy(String pol) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final AtomicBoolean excelCreado = new AtomicBoolean(false); // Almacenar el estado de excelCreado

        // Crear un nuevo hilo para ejecutar la simulación
        Thread simulationThread = new Thread(() -> {
            double[][] matriz;
            if (pol.equals("A")) {
                matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "A");
                costoPromedio[0] = matriz[matriz.length - 1][15];
                nombreArchivo = "SimulacionPoliticaA.xls";
            } else {
                matriz = VectorEstado.generadorVectoresParImpar(cantidadDiasSimulacion, stockInicial, diasEntrePedidos, cantidadAPedir, "B");
                costoPromedio[1] = matriz[matriz.length - 1][15];
                nombreArchivo = "SimulacionPoliticaB.xls";
            }
            excelCreado.set(GeneradorExcel.crearExcel(nombreArchivo, matriz)); // Actualizar el estado de excelCreado

            Platform.runLater(() -> {
                if (excelCreado.get()) { // Mostrar la ventana de éxito solo si excelCreado es true
                    progressBarStage.close();
                    ventanaTablaCreadaExitosamente();
                }
                if (scheduler != null) {
                    scheduler.shutdown();
                }
            });
        });

        // Inicia la simulación
        simulationThread.start();

        // Programa una tarea para mostrar la progressBar después de 5 segundos si la simulación aún está en ejecución y excelCreado es true
        scheduler.schedule(() -> {
            if (simulationThread.isAlive() && !excelCreado.get()) {
                Platform.runLater(() -> {
                    showProgressBar();
                });
            }
        }, 5, TimeUnit.SECONDS);
    }

    
}

