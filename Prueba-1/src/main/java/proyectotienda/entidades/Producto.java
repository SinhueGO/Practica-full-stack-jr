package proyectotienda.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Min(value = 0, message = "El precio debe ser mayor a 0")
    private Double precio;

    @Min(value = 0, message = "Las existencias no pueden ser negativas")
    private Integer existencias;

    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @PrePersist
    @PreUpdate
    public void validarActivo() {

        this.activo = (this.existencias != null && this.existencias > 0);
    }
}

