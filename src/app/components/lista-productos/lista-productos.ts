import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
@Component({
  selector: 'app-lista-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-productos.html',
  styleUrl: './lista-productos.css',
})

export class ListaProductos implements OnInit {
  productos: any[] = []; 

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.productoService.listar().subscribe({
      next: (data: any) => this.productos = data.content || data,
      error: (e) => console.error(e)
    });
  }
}
