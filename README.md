# Simulación de Modelos de Gestión de Stock

Este proyecto tiene como objetivo evaluar dos políticas de gestión de stock para determinar cuál es más conveniente en términos de costos. Se utiliza una simulación que abarca un período de 300 días, tomando en cuenta las características y restricciones de cada política, así como las probabilidades de demanda y demora.

## Descripción del Problema

La empresa desea comparar las siguientes dos políticas de gestión de stock:

### **Política A**
- Realiza un pedido fijo de **15 decenas** cada **10 días**.

### **Política B**
- Realiza un pedido cada **20 días**, con una cantidad igual a la **demanda total de los 20 días anteriores**, incluido el día que hace el pedido.

## Datos del Modelo

### **Demanda**
La demanda es aleatoria y se distribuye según la siguiente tabla:

| **Demanda (en decenas)** | 0  | 1  | 2  | 3  | 4  | 5  |
|---------------------------|----|----|----|----|----|----|
| **Probabilidad (%)**      | 5  | 12 | 18 | 25 | 22 | 18 |

### **Demora de Pedidos**
El tiempo que tarda un pedido en ingresar al almacén varía entre 1 y 4 días, con la siguiente distribución de probabilidad:

| **Demora (en días)** | 1   | 2   | 3   | 4   |
|-----------------------|-----|-----|-----|-----|
| **Probabilidad (%)**  | 15  | 20  | 40  | 25  |

### **Costos Asociados**
- **Costo de almacenamiento:** $5.00 por día y por unidad de producto.
- **Costo de faltante:** $9.00 por día y por unidad de producto.
- **Costo de pedido:** Depende de la cantidad de unidades solicitadas:
  - **0 – 15 decenas:** $20.
  - **16 – 40 decenas:** $30.
  - **Más de 40 decenas:** $40.

### **Estado Inicial**
- Stock inicial: **20 decenas**.
- Día 1:
  - Política A: Pedido de **15 decenas**.
  - Política B: Pedido de **25 decenas**.

## Objetivo
El objetivo de la simulación es evaluar el costo total de cada política durante un período de **300 días**, considerando los costos de almacenamiento, faltantes y pedidos. Al final, se seleccionará la política más conveniente en términos de costos.
