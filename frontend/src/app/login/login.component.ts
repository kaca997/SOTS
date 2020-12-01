import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ToastrService } from 'ngx-toastr';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	form: FormGroup;

	constructor(
		private fb: FormBuilder,
		private router: Router,
		private authenticationService: AuthenticationService,
		private toastr: ToastrService
	) {
		this.form = this.fb.group({
			username: [null, Validators.required],
			password: [null, Validators.required]
		});
	}

	ngOnInit() {
	}

	submit() {
		const auth: any = {};
		const jwt: JwtHelperService = new JwtHelperService();
		auth.username = this.form.value.username;
		auth.password = this.form.value.password;
		this.authenticationService.login(auth).subscribe(
			result => {
				this.toastr.success('Login successfull!');
				// alert('Login successfull!')
				localStorage.setItem('user', result);
				//console.log(result);
				//console.log(jwt.decodeToken(result));
				let info = jwt.decodeToken(result)
				console.log(info);
				if (info.role == "ROLE_TEACHER") {
					this.router.navigate(['new-test'])
				}
				else{
					this.router.navigate(['courses']);
				}
				
			},
			error => {
				console.log(error);
        		this.toastr.error(error.error);
			}
		);
	}
}