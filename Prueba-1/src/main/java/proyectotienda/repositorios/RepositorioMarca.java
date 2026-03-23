package proyectotienda.repositorios;

import proyectotienda.entidades.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioMarca extends JpaRepository<Marca, Integer> {
}
