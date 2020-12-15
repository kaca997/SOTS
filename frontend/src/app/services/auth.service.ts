import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
	providedIn: 'root'
})
export class AuthenticationService {
	private headers = new HttpHeaders({ 'Content-Type': 'application/json',
										'Access-Control-Allow-Origin': '*',
										'Access-Control-Allow-Credentials': 'true'});
	constructor(
		private http: HttpClient
	) { }

	login(auth: any): Observable<any> {
		let loginUrl =  "http://localhost:8080/auth/login";
		return this.http.post(loginUrl, {username: auth.username, password: auth.password}, {headers: this.headers, responseType: 'text'});
	}

	logout(): Observable<any> {
		let logoutUrl = "http://localhost:8080/auth/logout";
		return this.http.get(logoutUrl, {headers: this.headers, responseType: 'text'});
	}

	isLoggedIn(): boolean {
		if (localStorage.getItem('user')) {
				return true;
		}
		return false;
  }
  	isTeacher(): boolean {
		const token = localStorage.getItem('user');
		const jwt: JwtHelperService = new JwtHelperService();
		const info = jwt.decodeToken(token);
		if (info.role == "ROLE_TEACHER") {
			return true;
		}
		return false;
	}

	getRole(): string {
		const token = localStorage.getItem('user');
		const jwt: JwtHelperService = new JwtHelperService();
		const info = jwt.decodeToken(token);
		return info.role;
	}

	getUsername(): string {
		const token = localStorage.getItem('user');
		const jwt: JwtHelperService = new JwtHelperService();
		const info = jwt.decodeToken(token);
		return info.sub;
	}
}