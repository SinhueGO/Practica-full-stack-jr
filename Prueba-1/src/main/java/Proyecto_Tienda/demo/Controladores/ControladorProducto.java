package Proyecto_Tienda.demo.Controladores;

import Proyecto_Tienda.demo.Entidades.Producto;
import Proyecto_Tienda.demo.Repositorios.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/productos")
@CrossOrigin(origins = "http://Localhost:4200")
public class ControladorProducto {
    @Autowired
    private RepositorioProducto repositorio;

    // Obtener todos los productos
    @GetMapping
    public List<Producto> listar() {
        return repositorio.findAll();
    }
}
