package org.example;

import java.io.*;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GeneradorExcel {
    public static boolean crearExcel(String nombreArchivo, double[][] datos) {
        @SuppressWarnings("resource")
        Workbook wb = new XSSFWorkbook();
        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream(nombreArchivo);

            // Crear hoja
            Sheet sheet = wb.createSheet("Tabla Montecarlo");

            // Crear títulos de columnas
            String[] columnTitles = {"Día", "RND1", "Demanda", "RND2", "Días Demora", "Día Llegada", "Stock", "¿Pido?", "Demanda ++", "¿Cuánto?", "Costo Orden", "Costo Mantenimiento", "Costo Faltante", "Costo Total", "Costo Acumulado", "Costo Promedio"};

            // Crear la fila de títulos y aplicar estilo
            Row titleRow = sheet.createRow(0);
            CellStyle titleStyle = wb.createCellStyle();
            Font titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.WHITE.getIndex());
            titleStyle.setFont(titleFont);
            titleStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setBorderTop(BorderStyle.THIN);
            titleStyle.setBorderBottom(BorderStyle.THIN);
            titleStyle.setBorderLeft(BorderStyle.THIN);
            titleStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < columnTitles.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columnTitles[i]);
                cell.setCellStyle(titleStyle);
            }

            // Agregar datos y aplicar estilo a las celdas
            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            for (int fila = 0; fila < datos.length; fila++) {
                Row row = sheet.createRow(fila + 1);
                for (int columna = 0; columna < datos[fila].length; columna++) {
                    Cell cell = row.createCell(columna);

                    if (columna == 3 || columna == 4 || columna == 5) {
                        // Para las columnas 3, 4 y 5, transformar 0 en cadena vacía
                        if (datos[fila][columna] == 0.0) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(datos[fila][columna]);
                        }
                    } else if (columna == 7) {
                        // Transformar 1 y 0 en "SI" y "NO" para la columna "¿Pido?"
                        String valor = datos[fila][columna] == 1.0 ? "SI" : "NO";
                        cell.setCellValue(valor);
                    } else {
                        // Para otras columnas, escribir el valor numérico
                        cell.setCellValue(datos[fila][columna]);
                    }
                    
                    cell.setCellStyle(dataStyle);
                }
            }

            // Ajustar el ancho de las columnas automáticamente
            for (int i = 0; i < columnTitles.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Mostrar mensaje en la consola para la ejecución exitosa del programa
            System.out.println("Tabla creada exitosamente");

            wb.write(fileOut);
            return true;
        } catch (IOException e) {
            // No se puede abrir o cerrar el archivo
            System.out.println("No se puede generar nuevamente el excel mientras el archivo está abierto. Cierrelo antes de volver a generar");
            JOptionPane.showMessageDialog(null, "No se puede generar nuevamente el excel mientras el archivo está abierto. Cierrelo antes de volver a generar", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
