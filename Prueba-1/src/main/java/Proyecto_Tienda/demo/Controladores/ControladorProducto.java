package Proyecto_Tienda.demo.Controladores;
import Proyecto_Tienda.demo.Entidades.Producto;
import Proyecto_Tienda.demo.Repositorios.RepositorioProducto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Productos", description = "Endpoints para la gestión y ajuste de inventario de productos")
public class ControladorProducto {

    @Autowired
    private RepositorioProducto repositorio;

    // 1. GET: Listado con paginación [Requisito de la actividad]
    @Operation(summary = "Listar productos", description = "Obtiene la lista paginada de productos")
    @GetMapping
    public Page<Producto> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    // 2. POST: Crear producto con validaciones [Requisito de la actividad]
    @Operation(summary = "Crear producto", description = "Valida nombre único y precio positivo")
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Producto producto) {
        if (repositorio.existsByNombre(producto.getNombre())) {
            return ResponseEntity.badRequest().body("Error: El nombre del producto ya existe.");
        }
        return ResponseEntity.ok(repositorio.save(producto));
    }

    // 3. GET: Detalle de producto
    @Operation(summary = "Ver detalle", description = "Obtiene un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> detalle(@PathVariable Integer id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. PATCH: Activar/Desactivar [Requisito de la actividad]
    @Operation(summary = "Activar/Desactivar", description = "Alterna el estado activo del producto")
    @PatchMapping("/{id}/activar")
    public Producto cambiarEstado(@PathVariable Integer id) {
        Producto p = repositorio.findById(id).orElseThrow();
        p.setActivo(!p.getActivo()); // Invierte el estado actual
        return repositorio.save(p);
    }

    // 5. POST: Ajuste de inventario [Requisito de la actividad - Corregido para evitar Error 500]
    @Operation(summary = "Ajuste de inventario", description = "Suma o resta existencias. Requiere 'cantidad' y 'razon'")
    @PostMapping("/{id}/ajustar")
    public ResponseEntity<?> ajustarInventario(@PathVariable Integer id, @RequestBody Map<String, Object> ajuste) {
        try {
            Producto p = repositorio.findById(id).orElseThrow();

            // Validación de la razón (Requisito obligatorio)
            if (!ajuste.containsKey("razon") || ajuste.get("razon").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Debe proporcionar una razón para el ajuste.");
            }

            // Conversión segura de tipos para evitar el error 500 visto en Swagger
            Integer cantidad = Integer.parseInt(ajuste.get("cantidad").toString());

            p.setExistencias(p.getExistencias() + cantidad);
            repositorio.save(p);
            return ResponseEntity.ok(p);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en los datos enviados: " + e.getMessage());
        }
    }
}