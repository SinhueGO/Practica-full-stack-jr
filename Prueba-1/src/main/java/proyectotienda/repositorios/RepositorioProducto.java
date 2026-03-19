package proyectotienda.repositorios;

import proyectotienda.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioProducto extends JpaRepository<Producto, Integer> {
    boolean existsByNombre(String nombre);

}
