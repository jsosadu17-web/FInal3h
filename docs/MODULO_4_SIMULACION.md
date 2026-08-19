# Módulo 4: Simulación de escenarios y proyección de flujo de caja

## Objetivo

Permitir crear escenarios por empresa, aplicar variaciones porcentuales a los promedios históricos de ingresos y egresos, proyectar saldos por periodos semanales o mensuales y dejar los resultados disponibles para indicadores.

## Arquitectura y relaciones

`REST Controller → Service interface → Service implementation → Repository → Entity`.

`EscenarioEntity` pertenece a `EmpresaEntity`. Cada `ProyeccionFlujoCajaEntity` pertenece al escenario y conserva también la empresa para consultas eficientes. Los movimientos existentes se consultan por `MovimientoCajaEntity → CuentaCajaEntity → EmpresaEntity`; no se creó una entidad paralela.

## Endpoints

- `GET /api/escenarios`
- `GET /api/escenarios/{id}`
- `GET /api/escenarios/empresa/{empresaId}`
- `POST /api/escenarios`
- `PUT /api/escenarios/{id}`
- `DELETE /api/escenarios/{id}` — desactiva lógicamente.
- `POST /api/simulaciones`
- `GET /api/simulaciones/escenario/{escenarioId}`
- `GET /api/simulaciones/empresa/{empresaId}`
- `DELETE /api/simulaciones/escenario/{escenarioId}`

## Fórmulas y supuestos

Se cuentan meses inclusivos para `MENSUAL` y semanas de siete días inclusivas para `SEMANAL`; siempre se usa al menos un periodo histórico. Los promedios son `total histórico / periodos históricos`. Luego se aplica `base × (1 + variación / 100)`, el flujo neto es ingreso menos egreso y el saldo final se arrastra como saldo inicial del siguiente periodo. Los importes nuevos usan `BigDecimal`, escala 2 y `HALF_UP`. El saldo de cuentas existente es `Double`, por lo que se convierte a `BigDecimal` al iniciar la simulación.

La simulación exige al menos una cuenta y movimientos en el rango. Si `guardarResultado=true`, elimina las proyecciones previas del escenario y guarda el nuevo conjunto en la misma transacción.

## JSON de ejemplo

```json
{
  "nombre": "Caída de ventas y aumento de gastos",
  "descripcion": "Escenario pesimista para evaluar liquidez",
  "empresaId": 1,
  "variacionIngresosPorcentaje": -10.00,
  "variacionEgresosPorcentaje": 15.00,
  "numeroPeriodos": 3,
  "tipoPeriodo": "MENSUAL"
}
```

```json
{
  "escenarioId": 1,
  "fechaInicioHistorial": "2026-01-01",
  "fechaFinHistorial": "2026-07-31",
  "guardarResultado": true
}
```

## Integración

Consume la empresa y cuentas de los módulos base, y los movimientos/categorías existentes de los módulos 2 y 3. El módulo 5 puede consultar las proyecciones persistidas por escenario o empresa.

## Postman

Importar `docs/postman/modulo4_simulacion.postman_collection.json`, levantar la aplicación en `http://localhost:7070` y reemplazar `empresaId`/`escenarioId` por IDs existentes.

## Limitaciones actuales

- El proyecto conserva el tipo `Double` del saldo histórico existente para no romper módulos previos.
- No se agregan datos de prueba automáticos porque el proyecto no usa `data.sql` ni un perfil de desarrollo.
- La conexión MySQL debe estar disponible para las pruebas de contexto y ejecución real.
