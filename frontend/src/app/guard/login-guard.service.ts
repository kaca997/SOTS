import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';

@Injectable({
	providedIn: 'root'
})
export class LoginGuard implements CanActivate {

	constructor(
		public auth: AuthenticationService,
		public router: Router
	) { }

	canActivate(): boolean {
		if (this.auth.isLoggedIn()) {
			this.router.navigate(['/dashboard']);
			return false;
		}
		return true;
	}
}