import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrModule } from "ngx-toastr";

import { SidebarModule } from './sidebar/sidebar.module';
import { FooterModule } from './shared/footer/footer.module';
import { NavbarModule} from './shared/navbar/navbar.module';
import { FixedPluginModule} from './shared/fixedplugin/fixedplugin.module';

import { AppComponent } from './app.component';
import { AppRoutes } from './app.routing';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Interceptor } from './interceptors/intercept.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './core/material/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';


@NgModule({
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    LoginComponent
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
    ToastrModule.forRoot(),
    FooterModule,
    FixedPluginModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
