package Proyecto_Tienda.demo.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Producto")
@Data
public class CdataBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private Double precio;
    private Integer existencias;
    private Boolean activo;
}
