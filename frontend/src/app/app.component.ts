import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  constructor(private authenticationService: AuthenticationService, private router:Router) {}
  ngOnInit(){
    if(!this.authenticationService.isLoggedIn()){
    this.router.navigate(['login'])
    }
  }
}
