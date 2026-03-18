package Proyecto_Tienda.demo.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marcas")
@Data
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Marca;

    @Column(name = "marca_Name")
    private String nombre;
}
