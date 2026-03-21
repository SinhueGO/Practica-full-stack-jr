import { Routes } from '@angular/router';
import { ListaProductos } from './components/lista-productos/lista-productos';

export const routes: Routes = [
  { path: '', component: ListaProductos },
  { path: '**', redirectTo: '' }
];