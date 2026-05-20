import { Routes } from '@angular/router';

import { Administrador } from './administrador/administrador';
import { VitrineApi } from './vitrine/vitrine-api';

export const routes: Routes = [
  { path: '', component: VitrineApi },
  { path: 'admin', component: Administrador },
  { path: 'cadastro', redirectTo: 'admin', pathMatch: 'full' },
];
