import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) { }

  listar(): Observable<any> { return this.http.get(this.apiUrl); }

  crear(producto: any): Observable<any> { return this.http.post(this.apiUrl, producto); }

  actualizar(id: number, producto: any): Observable<any> {return this.http.put(`${this.apiUrl}/${id}`, producto);}

  eliminar(id: number): Observable<any> {return this.http.delete(`${this.apiUrl}/${id}`); }
}