package Proyecto_Tienda.demo.Servicios;

import Proyecto_Tienda.demo.Entidades.Producto;
import Proyecto_Tienda.demo.Repositorios.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioProducto {

    @Autowired
    private RepositorioProducto repositorio;

    @Transactional
    public Producto ajustarStock(Integer id, Integer cantidad, String razon) {

        Producto p = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));


        if (razon == null || razon.trim().isEmpty()) {
            throw new RuntimeException("La razón es obligatoria para el ajuste");
        }


        p.setExistencias(p.getExistencias() + cantidad);


        return repositorio.save(p);
    }
}
