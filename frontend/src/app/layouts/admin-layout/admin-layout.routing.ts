import { Routes } from '@angular/router';
import { CoursesComponent } from 'app/pages/courses/courses.component';
import { RoleGuard } from 'app/guard/role-guard.service';
import { NewTestComponent } from 'app/pages/new-test/new-test.component';
import { TestDetailsComponent } from 'app/pages/test-details/test-details.component';
import { TestPreviewComponent } from 'app/pages/test-preview/test-preview.component';
export const AdminLayoutRoutes: Routes = [
   { path: "new-test",       component: NewTestComponent },
   { path: "courses",       component: CoursesComponent },
   { path: 'testsToDo/:id', component: TestPreviewComponent },
   { path: 'testsDone/:id', component: TestPreviewComponent },
   { path: 'teacherTests/:id', component: TestPreviewComponent },
   { path: 'testInProgress/:id', component: TestDetailsComponent },
   { path: 'doneTestDetails/:id', component: TestDetailsComponent }, 
   { path: 'testDetailsTeacher/:id', component: TestDetailsComponent },   
   { path: "new-test",       component: NewTestComponent,
   data: { expectedRoles: 'ROLE_TEACHER' },
   canActivate: [RoleGuard] },
];
