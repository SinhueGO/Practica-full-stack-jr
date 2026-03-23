package proyectotienda.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyectotienda.entidades.Marca;
import proyectotienda.repositorios.RepositorioMarca;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "http://localhost:4400")
public class ControladorMarca {

    @Autowired
    private RepositorioMarca repositorioMarca;

    @GetMapping
    public List<Marca> listar() {
        return repositorioMarca.findAll();
    }
}