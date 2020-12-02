import { Routes } from '@angular/router';
import { LoginGuard } from './guard/login-guard.service';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginComponent } from './login/login.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';

export const AppRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginGuard]
  },
  {
    path: '',
    redirectTo: 'courses',
    pathMatch: 'full',
  }, 
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
        {
      path: '',
      loadChildren: './layouts/admin-layout/admin-layout.module#AdminLayoutModule'
  }]
},
{ path: 'not-found', component: ErrorPageComponent},
{
  path: "**",
  redirectTo: "not-found"
}
]
