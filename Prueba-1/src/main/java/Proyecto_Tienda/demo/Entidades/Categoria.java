package Proyecto_Tienda.demo.Entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Categoria;

    @Column(name = "categorias_Name")
    private String nombre;
}
