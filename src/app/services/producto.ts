import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos'; // URL de tu Spring Boot

  constructor(private http: HttpClient) { }

  // 1. Listar con paginación
  listar(page: number, size: number): Observable<any> {
    return this.http.get(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  // 2. Crear producto
  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  // 3. Obtener detalle
  obtenerDetalle(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  // 4. Actualizar
  actualizar(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  // 5. Activar/Desactivar
  cambiarEstado(id: number): Observable<Producto> {
    return this.http.patch<Producto>(`${this.apiUrl}/${id}/activar`, {});
  }

  // 6. Ajustar Stock
  ajustarStock(id: number, cantidad: number, razon: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${id}/ajustar`, { cantidad, razon });
  }
}