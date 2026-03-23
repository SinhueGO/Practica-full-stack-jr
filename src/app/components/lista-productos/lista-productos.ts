import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-lista-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-productos.html',
  styleUrl: './lista-productos.css',
})
export class ListaProductos implements OnInit {
  productos: any[] = [];
  marcas: any[] = [];
  categorias: any[] = [];
  
  nuevoProducto: any = {
    nombre: '',
    precio: 0,
    existencias: 0,
    marca: null,
    categoria: null 
  };

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void { 
    this.cargarProductos();
    this.cargarRelaciones();
  }

  cargarProductos(): void {
    this.productoService.listar().subscribe(data => {
      this.productos = data.content ? data.content : data;
    });
  }
  cargarRelaciones(): void {
    this.productoService.listarMarcas().subscribe(data => this.marcas = data);
    this.productoService.listarCategorias().subscribe(data => this.categorias = data);
  }

  guardar(): void {
    if (this.nuevoProducto.id) {
      this.productoService.actualizar(this.nuevoProducto.id, this.nuevoProducto).subscribe(() => {
        this.finalizarOperacion();
      });
    } else {
      this.productoService.crear(this.nuevoProducto).subscribe(() => {
        this.finalizarOperacion();
      });
    }
  }

  prepararEditar(producto: any): void {
    this.nuevoProducto = { ...producto };
  }

  finalizarOperacion(): void {
    this.cargarProductos();
    this.nuevoProducto = { nombre: '', precio: 0, existencias: 0, marca: null, categoria: null };
  }

  borrar(id: number): void {
    if(confirm('¿Seguro que deseas eliminarlo?')) {
      this.productoService.eliminar(id).subscribe(() => this.cargarProductos());
    }
  }

  cancelarEdicion(): void {
    this.nuevoProducto = { nombre: '', precio: 0, existencias: 0 };
  }
}