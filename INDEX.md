# INDEX: SistemaCaja
Stack: Java 21, Spring Boot 4.1.0, Spring Data JPA, MySQL, Thymeleaf, Validation y Lombok  
Actualizado: 2026-08-05

## Estructura

- `src/main/java/com/example/SistemaCaja/` — paquete raíz y aplicación Spring Boot.
- `controllers/` — controllers MVC existentes y REST de categorías.
- `entities/` — entidades JPA de empresa, cuenta, categoría y movimiento.
- `models/` — modelos/DTOs usados por los módulos existentes.
- `repositories/` — interfaces Spring Data JPA.
- `services/` — interfaces de servicio.
- `services/implementation/` — implementaciones y mapeos entidad-modelo.
- `src/main/resources/templates/` — vistas Thymeleaf existentes.
- `src/test/` — pruebas actuales.

## Configuración

- `pom.xml` — Java 21 y dependencias Spring Boot/JPA/MySQL/Thymeleaf/Validation/Lombok.
- `src/main/resources/application.properties` — MySQL `bd_sistema_caja`, puerto 7070 y `ddl-auto=update`.

## Modelo existente

- `EmpresaEntity` 1:N `CuentaCajaEntity` mediante `cuentas` / `empresa`.
- `CuentaCajaEntity` tiene `saldo` como `Double`.
- `MovimientoCajaEntity` tiene `monto` como `BigDecimal`, `fechaMovimiento` como `LocalDateTime` y `tipo` como texto `INGRESO` o `EGRESO`.
- `MovimientoCajaEntity` se relaciona con `CuentaCajaEntity` y `CategoriaMovimientoEntity`.

## APIs existentes

- Vistas MVC: `/empresas`, `/cuentas`, `/categorias-movimiento`, `/movimientos-caja`.
- REST existente: `/api/categorias-movimiento`.
- El módulo 4 agregará `/api/escenarios` y `/api/simulaciones`.

## Estado

- [x] Módulos base de empresa, cuentas, categorías y movimientos.
- [ ] Simulación de escenarios y proyección de flujo de caja.
