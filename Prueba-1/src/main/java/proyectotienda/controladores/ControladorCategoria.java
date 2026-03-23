package proyectotienda.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyectotienda.entidades.Categoria;
import proyectotienda.repositorios.RepositorioCategoria;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4400")
public class ControladorCategoria {

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @GetMapping
    public List<Categoria> listar() {
        return repositorioCategoria.findAll();
    }
}
