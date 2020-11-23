import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class Interceptor implements HttpInterceptor {

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

		const item = localStorage.getItem('user');
		//console.log(item);

		if (item) {
			const cloned = req.clone({
				headers: req.headers.set('Authorization', 'Bearer ' + item)
			});

			return next.handle(cloned);
		} else {
			return next.handle(req);
		}
	}
}
