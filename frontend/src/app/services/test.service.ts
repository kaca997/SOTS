import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TestService {
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Credentials': 'true'
  });
  constructor(
    private http: HttpClient
  ) { }
  
  createTest(test: any) {
    let testUrl =  "http://localhost:8080/test/create";
		return this.http.post(testUrl, test, {headers: this.headers});
  }

}
