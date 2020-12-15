import { Routes } from '@angular/router';
import { CoursesComponent } from 'app/pages/courses/courses.component';
import { RoleGuard } from 'app/guard/role-guard.service';
import { NewTestComponent } from 'app/pages/new-test/new-test.component';
import { TestDetailsComponent } from 'app/pages/test-details/test-details.component';
import { TestPreviewComponent } from 'app/pages/test-preview/test-preview.component';
import { GraphComponent } from 'app/pages/graph/graph.component';
export const AdminLayoutRoutes: Routes = [
   // { path: "new-test",       component: NewTestComponent },
   { path: "courses",       component: CoursesComponent,
   data: { expectedRoles: 'ROLE_STUDENT|ROLE_TEACHER' },
   canActivate: [RoleGuard]  },
   { path: 'testsToDo/:id', component: TestPreviewComponent,
   data: { expectedRoles: 'ROLE_STUDENT' },
   canActivate: [RoleGuard]  },
   { path: 'testsDone/:id', component: TestPreviewComponent,
   data: { expectedRoles: 'ROLE_STUDENT' },
   canActivate: [RoleGuard]  },
   { path: 'teacherTests/:id', component: TestPreviewComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard]  },
   { path: 'testInProgress/:id', component: TestDetailsComponent,
   data: { expectedRoles: 'ROLE_STUDENT' },
   canActivate: [RoleGuard]  },
   { path: 'doneTestDetails/:id', component: TestDetailsComponent,
   data: { expectedRoles: 'ROLE_STUDENT' },
   canActivate: [RoleGuard]  }, 
   { path: 'testDetailsTeacher/:id', component: TestDetailsComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard]  },   
   { path: "new-test",       component: NewTestComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard] },
   { path: "new-domain",       component: GraphComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard] },
];
