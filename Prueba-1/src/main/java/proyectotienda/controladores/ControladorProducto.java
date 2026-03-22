package proyectotienda.controladores;

import proyectotienda.entidades.Producto;
import proyectotienda.repositorios.RepositorioProducto;
import proyectotienda.servicios.ServicioProducto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;


@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4400")
@Tag(name = "Productos", description = "Endpoints para la gestión y ajuste de inventario de productos")
public class ControladorProducto {

    @Autowired
    private RepositorioProducto repositorio;

    @Autowired
    private ServicioProducto servicio;


    // 1. GET
    @Operation(summary = "Listar productos con paginación")
    @GetMapping
    public Page<Producto> listar(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    // 2. POST
    @Operation(summary = "Crear un nuevo producto")
    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Producto producto) {
        if (repositorio.existsByNombre(producto.getNombre())) {
            return ResponseEntity.badRequest().body("Error: El nombre del producto ya existe.");
        }
        if (producto.getPrecio() <= 0) {
            return ResponseEntity.badRequest().body("Error: El precio debe ser mayor a 0.");
        }
        return ResponseEntity.ok(repositorio.save(producto));
    }

    // 3. GET
    @Operation(summary = "Obtener detalle de un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> detalle(@PathVariable Integer id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. PUT
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto datosNuevos) {
        return repositorio.findById(id).map(p -> {
            p.setNombre(datosNuevos.getNombre());
            p.setPrecio(datosNuevos.getPrecio());
            p.setExistencias(datosNuevos.getExistencias());
            if (datosNuevos.getMarca() != null) p.setMarca(datosNuevos.getMarca());
            if (datosNuevos.getCategoria() != null) p.setCategoria(datosNuevos.getCategoria());

            return ResponseEntity.ok(repositorio.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. PATCH
    @Operation(summary = "Activar o desactivar un producto")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Producto> cambiarEstado(@PathVariable Integer id) {
        return repositorio.findById(id).map(p -> {
            p.setActivo(!p.getActivo());
            return ResponseEntity.ok(repositorio.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. POST
    @Operation(summary = "Ajuste de inventario (Requiere cantidad y razón)")
    @PostMapping("/{id}/ajustar")
    public ResponseEntity<Object> ajustar(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("cantidad") || !body.containsKey("razon")) {
                return ResponseEntity.badRequest().body("Error: Debe proporcionar 'cantidad' y 'razon'.");
            }
            Integer cant = Integer.parseInt(body.get("cantidad").toString());
            String raz = body.get("razon").toString();

            return ResponseEntity.ok(servicio.ajustarStock(id, cant, raz));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en el ajuste: " + e.getMessage());
        }
    }

    // 7. DELETE
    @Operation(summary = "Eliminar un producto permanentemente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Integer id) {
        return repositorio.findById(id).map(p -> {
            repositorio.delete(p);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}