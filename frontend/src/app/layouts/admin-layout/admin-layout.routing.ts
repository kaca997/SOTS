import { Routes } from '@angular/router';
import { RoleGuard } from 'app/guard/role-guard.service';

import { NewTestComponent } from 'app/pages/new-test/new-test.component';

export const AdminLayoutRoutes: Routes = [
   { path: "new-test",       component: NewTestComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard] },
];
