import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Credentials': 'true'
  });
  constructor(
    private http: HttpClient
  ) { }
  
  getAllCoursesForUser(role: string): Observable<any> { 
		const courseUrl = `http://localhost:8080/course/getAllFor${role}`;
		return this.http.get(courseUrl, {headers: this.headers});
  }
  

  getCoursesWithoutDomain(): Observable<any> { 
		const courseUrl = `http://localhost:8080/course/getCoursesWithoutDomain`;
		return this.http.get(courseUrl, {headers: this.headers});
  }

  getProblemsByCourse(id: number) : Observable<any> { 
    const courseUrl = `http://localhost:8080/course/getProblemsByCourse/${id}`;
		return this.http.get(courseUrl, {headers: this.headers});
  }
}
