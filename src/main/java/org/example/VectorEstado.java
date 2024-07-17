package org.example;

import java.util.Random;


public class VectorEstado {

    // Método para calcular un valor RND entre 0 y 1
    private static double calcularRandom() {
        Random random = new Random();
        double rnd = random.nextDouble();
        String rndString = String.format("%.4f", rnd).replace(",", ".");
        return Double.parseDouble(rndString);
    }

    // Método para calcular la demanda
    private static int calcularDemanda(double rnd) {
        // Rangos de RND basados en las probabilidades acumuladas
        double[] limites = {0.05, 0.17, 0.35, 0.60, 0.82, 1.00};
        // Demanda correspondiente a cada rango
        int[] demandas = {0, 1, 2, 3, 4, 5};
    
        // Determinar la demanda basada en el valor RND
        for (int i = 0; i < limites.length; i++) {
            if (rnd <= limites[i]) {
                return demandas[i];
            }
        }
        return 0; // Valor por defecto en caso de que rnd no caiga en ningún rango (no debería suceder)
    }

    // Método para calcular la demora
    private static int calcularDiasDeDemora(double rnd, int huboPedido) {
        // Rangos de RND basados en las probabilidades acumuladas
        double[] limites = {0.15, 0.35, 0.75, 1.00};
        // Demora correspondiente a cada rango
        int[] demoras = {1, 2, 3, 4};
    
        // Determinar la demora basada en el valor RND
        for (int i = 0; i < limites.length; i++) {
            if (rnd <= limites[i]) {
                if (huboPedido == 0) {
                    return 0; // Si no hubo pedido, la demora es 0
                } else {
                    return demoras[i]; // Retornar demora en días
                }
            }
        }
        return 0; // Valor por defecto en caso de que rnd no caiga en ningún rango (no debería suceder)
    }

    // Método para calcular el día de llegada
    private static int calcularDiaDeLlegada(int diaActual, int diasDemora, int huboPedido) {
        if (huboPedido == 0) {
            return 0; // Si no hubo pedido, no hay día de llegada
        } else {
            return diaActual + diasDemora;
        }
    }

    // Método para calcular el stock
    private static int calcularStock(int stockAnterior, int demanda, int cantidadAPedir, int diaActual, int diaLlegada) {
        int stock = 0;
        if (diaActual == diaLlegada) {
                stock = stockAnterior - demanda + cantidadAPedir; // Sumar la cantidad pedida al stock
            } else {
                stock = stockAnterior - demanda; // Restar la demanda al stock
            }
        if (stock < 0) {
            return 0; // Si el stock es negativo, retornar 0
        } else {
            return stock;
        }
    }

    // Método para determinar si se debe pedir
    private static int determinarSiPedir(int diaActual, int diasEntrePedidos) {
        if (diaActual == 0) {
            return 0; // No se pide en el día 0
        } else {
            if (diaActual == 1) {
                return 1; // Se pide en el día 1
            } else {
                return (diaActual - 1) % diasEntrePedidos == 0 ? 1 : 0; // Se pide cada N días
            }
        }
    }

    // Método para calcular demanda acumulada
    private static int calcularDemandaAcumulada(int demanda, int demandaAcumulada, int huboPedidoAyer, int diaActual) {
        if (diaActual == 1) {
            return demanda; // En el día 1, la demanda acumulada es igual a la demanda
        } else  if (diaActual == 2) {
            return demanda + demandaAcumulada; // En el día 2, la demanda acumulada es la demanda más la demanda acumulada del día anterior
        } else {
            return huboPedidoAyer == 1 ? demanda : demanda + demandaAcumulada; // Reiniciar la demanda acumulada si hubo pedido el día anterior
        }
    }

    // Método para calcular la cantidad a pedir
    private static int calcularCantidadAPedir(int cantidadAPedir, String pol, int huboPedido, int diaActual, int demandaAcumuladaB) {
        if (pol == "A") {
            if (huboPedido == 0) {
                return 0; // No se pide si no hubo pedido
            } else {
                return cantidadAPedir; // Logica de la política A
            }
        } else {
            // Logica de la política B
            if (huboPedido == 0) {
                return 0; // No se pide si no hubo pedido
            } else {
                if (diaActual == 1) {
                    return cantidadAPedir; // Se pide en el día 1
                } else {
                    return demandaAcumuladaB; // Se pide la demanda acumulada
                }
            
            }
        }
    }

    // Método para calcular el costo de ordenar
    private static double calcularCostoDeOrdenar(int cantidadPedida, int huboPedido) {
        double costoPorDecena;
        
        if (huboPedido == 0) {
            return 0; // No hay costo de ordenar si no se pidió
        } else{
            // Determinar el costo por decena basado en la cantidad pedida
            if (cantidadPedida >= 0 && cantidadPedida <= 15) {
                costoPorDecena = 20;
            } else if (cantidadPedida >= 16 && cantidadPedida <= 40) {
                costoPorDecena = 30;
            } else { // Más de 40
                costoPorDecena = 40;
            }
        }

        return costoPorDecena;
    }

    // Método para calcular el costo de mantener
    private static double calcularCostoDeMantener(int stock) {
        return (stock * 5) * 10 ; // Costo de mantener por unidad (Parametrizar si es necesario)
    }

    // Método para calcular el costo por faltante
    private static double calcularCostoPorFaltante(int stockAnterior, int demanda, int cantidadAPedir, int diaActual, int diaLlegada) {
        if (diaActual == diaLlegada) {
            return 0; // No hay costo por faltante si llega el pedido
        } else {
            if (stockAnterior < demanda) {
                return ((demanda - stockAnterior) * 9) * 10; // Costo por faltante por unidad (Parametrizar si es necesario)
            } else {
                return 0; // No hay costo por faltante si el stock es suficiente
            }
        }
    }

    // Método para calcular el costo total
    private static double calcularCostoTotal(double costoDeOrdenar, double costoDeMantener, double costoPorFaltante) {
        return costoDeOrdenar + costoDeMantener + costoPorFaltante;
    }

    // Método para calcular el costo acumulado
    private static double calcularCostoAcumulado(double costoTotal, double costoAcumuladoAnterior) {
        return costoTotal + costoAcumuladoAnterior;
    }

    // Método para calcular el promedio de costo
    private static double calcularPromedioCosto(double costoAcumulado, int i) {
        if (i == 0) {
            return 0.0;
        }
        double promedio = costoAcumulado / i;
        String promedioString = String.format("%.4f", promedio).replace(",", ".");
        return Double.parseDouble(promedioString);
    }
        

    // Método para generar los vectores de estado par e impar
    public static double[][] generadorVectoresParImpar(int cantidadDiasSimulacion, int stockInicial, int diasEntrePedidos, int cantidadAPedirDada, String politica) {
        Random random = new Random();

        // Creación de los vectores
        double[] vectorPar = new double[16];
        double[] vectorImpar = new double[16];

        // Creación de la matriz para almacenar la información de las columnas
        double[][] matriz = new double[cantidadDiasSimulacion + 1][16];

        // Guardado del dia de llegada para poder calcular el stock en el dia de llegada
        int diaLlegadaReal = 0;
        int cantidadAPedirReal = 0;
        int demandaAcumuladaReal = 0;
    
        // Generación de valores aleatorios para los vectores
        for (int dias = 0; dias < (cantidadDiasSimulacion + 1); dias++) {
            double rnd1, rnd2, costoDeOrdenar, costoDeMantener, costoPorFaltante, costoTotal, costoAcumulado;
            int demanda, diasDemora, diaLlegada, stock, pido, demandaAcumulada, cantidadAPedir;

            if (dias == 0) {
                // En el día 0, no hay valores anteriores, solo se muestra el stock inicial
                rnd1 = 0.0;
                demanda = 0;
                rnd2 = 0.0;
                diasDemora = 0;
                diaLlegada = 0;
                stock = stockInicial;
                pido = 0;
                demandaAcumulada = 0;
                cantidadAPedir = 0;
                costoDeOrdenar = 0.0;
                costoDeMantener = 0.0;
                costoPorFaltante = 0.0;
                costoTotal = 0.0;
                costoAcumulado = 0.0;
            
            } else {
                double[] vectorAnterior = (dias + 1) % 2 == 0 ? vectorImpar : vectorPar;

                rnd1 = calcularRandom();
                demanda = calcularDemanda(rnd1);
                rnd2 = calcularRandom();
                pido = determinarSiPedir(dias, diasEntrePedidos);
                diasDemora = calcularDiasDeDemora(rnd2, pido);
                diaLlegada = calcularDiaDeLlegada(dias, diasDemora, pido);

                // Debo guardar el dia de llegada para poder calcular el stock en el día de llegada
                if (pido == 1) {
                    diaLlegadaReal = diaLlegada;
                }

                demandaAcumulada = calcularDemandaAcumulada(demanda, (int) vectorAnterior[8], (int) vectorAnterior[7], dias);

                // Debo guardar la demanda acumulada para poder calcular la cantidad a pedir
                if (pido == 1) {
                    demandaAcumuladaReal = demandaAcumulada;
                }

                cantidadAPedir = calcularCantidadAPedir(cantidadAPedirDada, politica, pido, dias, demandaAcumuladaReal);

                // Debo guardar la cantidad a pedir para poder calcular el stock en el día de llegada
                if (pido == 1) {
                    cantidadAPedirReal = cantidadAPedir;
                }

                costoDeOrdenar = calcularCostoDeOrdenar(cantidadAPedir, pido);
                stock = calcularStock((int) vectorAnterior[6], demanda, cantidadAPedirReal, dias, diaLlegadaReal);
                costoDeMantener = calcularCostoDeMantener(stock);
                costoPorFaltante = calcularCostoPorFaltante((int) vectorAnterior[6], demanda, cantidadAPedirDada, dias, diaLlegadaReal);
                costoTotal = calcularCostoTotal(costoDeOrdenar, costoDeMantener, costoPorFaltante);
                costoAcumulado = calcularCostoAcumulado(costoTotal, vectorAnterior[14]);
            }

            double costoPromedio = calcularPromedioCosto(costoAcumulado, dias);

            // Actualizar el vectorActual con todos los valores calculados
            double[] vectorActual = new double[16];
            vectorActual[0] = dias;
            vectorActual[1] = rnd1;
            vectorActual[2] = demanda;
            vectorActual[3] = rnd2;
            vectorActual[4] = diasDemora;
            vectorActual[5] = diaLlegada;
            vectorActual[6] = stock;
            vectorActual[7] = pido;
            vectorActual[8] = demandaAcumulada;
            vectorActual[9] = cantidadAPedir;
            vectorActual[10] = costoDeOrdenar;
            vectorActual[11] = costoDeMantener;
            vectorActual[12] = costoPorFaltante;
            vectorActual[13] = costoTotal;
            vectorActual[14] = costoAcumulado;
            vectorActual[15] = costoPromedio;


            // Asignar el vector actual al vector par o impar según corresponda
            if ((dias + 1) % 2 == 0) {
                vectorPar = vectorActual;
            } else {
                vectorImpar = vectorActual;
            }

            // Asignar el vector actual a la matriz directamente usando el índice 'dias'
            matriz[dias] = vectorActual;

            // if (dias == 0) {
            //     // En el día 0, no hay valores anteriores, solo se muestra el stock inicial
            //     System.out.println("Día: " + (int) vectorActual[0] + ", Stock inicial: " + (int) vectorActual[6]);
            // } else {
            //     System.out.println("Día: " + (int) vectorActual[0] + ", RND1: " + vectorActual[1] + ", Demanda: " + (int) vectorActual[2] + ", RND2: " + vectorActual[3] + ", Días de demora: " + (int) vectorActual[4] + ", Día de llegada: " + (int) vectorActual[5] + ", Stock: " + (int) vectorActual[6] + ", Pido: " + (int) vectorActual[7] + ", Demanda acumulada: " + (int) vectorActual[8] + ", Cantidad a pedir: " + (int) vectorActual[9] + ", Costo de ordenar: " + vectorActual[10] + ", Costo de mantener: " + vectorActual[11] + ", Costo por faltante: " + vectorActual[12] + ", Costo total: " + vectorActual[13] + ", Costo acumulado: " + vectorActual[14] + ", Costo promedio: " + vectorActual[15]);
            // }
        }


        return matriz;
    }


    // public static void main(String[] args) {
    //    double[][] matriz = generadorVectoresParImpar(300, 20, 20, 25, "B");
    //    GeneradorExcel.crearExcel("SimulacionPoliticaB.xls", matriz);
    //    for (int fila = 0; fila < matriz.length; fila++) {
    //        System.out.print("Fila " + fila + ": [ ");
    //        for (double value : matriz[fila]) {
    //            System.out.print(value + " ");
    //        }
    //        System.out.println("]");
    //    }
    // }
}
