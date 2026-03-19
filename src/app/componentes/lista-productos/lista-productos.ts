import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto';

@Component({
  selector: 'app-lista-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-productos.component.html',
  styleUrl: './lista-productos.component.css'
})
export class ListaProductosComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    // Usamos página 0 y tamaño 10 por defecto
    this.productoService.listar(0, 10).subscribe({
      next: (data) => {
        this.productos = data.content; // 'content' es donde Spring Boot guarda la lista en Page
      },
      error: (err) => console.error('Error al cargar productos', err)
    });
  }
}