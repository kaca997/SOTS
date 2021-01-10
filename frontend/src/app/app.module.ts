import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrModule } from "ngx-toastr";

import { SidebarModule } from './sidebar/sidebar.module';
import { NavbarModule} from './shared/navbar/navbar.module';

import { AppComponent } from './app.component';
import { AppRoutes } from './app.routing';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Interceptor } from './interceptors/intercept.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './core/material/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NewTestComponent } from './pages/new-test/new-test.component';
import { CoursesComponent } from './pages/courses/courses.component';
import { TestPreviewComponent } from './pages/test-preview/test-preview.component';
import { TestDetailsComponent } from './pages/test-details/test-details.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { GraphComponent } from './pages/graph/graph.component';
import { NewDomainComponent } from './pages/new-domain/new-domain.component';
import { KnowledgeSpacesViewComponent } from './pages/knowledge-spaces-view/knowledge-spaces-view.component';
import { XmlDisplay } from './pages/test-preview/test-preview.component';
import {MatDialogModule, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { StateGraphComponent } from './pages/state-graph/state-graph.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    LoginComponent,
    NewTestComponent,
    CoursesComponent,
    TestPreviewComponent,
    TestDetailsComponent,
    ErrorPageComponent,
    GraphComponent,
    NewDomainComponent,
    KnowledgeSpacesViewComponent,
    XmlDisplay,
    StateGraphComponent
  ],
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    RouterModule.forRoot(AppRoutes,{
      useHash: true
    }),
    SidebarModule,
    NavbarModule,
    MatDialogModule,
    ToastrModule.forRoot()
  ],
  entryComponents: [XmlDisplay],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true },
    {
      provide: MatDialogRef,
      useValue: {}
    },
    { provide: MAT_DIALOG_DATA, useValue: {} }],
  bootstrap: [AppComponent]
})
export class AppModule { }
