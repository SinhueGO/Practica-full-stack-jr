package Proyecto_Tienda.demo.Repositorios;

import Proyecto_Tienda.demo.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioProducto extends JpaRepository<Producto, Integer> {
    boolean existsByNombre(String nombre);

}
