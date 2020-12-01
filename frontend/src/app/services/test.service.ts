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

  getTestsToDoByCourse(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getCourseTestsToDo/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }

  getTestsDoneByCourse(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getDoneTests/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }

  getTeacherCourseTests(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getCourseTestsForTeacher/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }

  getTest(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getTest/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }

  getDoneTest(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getDoneTest/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }

  getTestTeacher(id:number) : Observable<any>{
    let testUrl = `http://localhost:8080/test/getTestTeacher/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }
  
  submitTest(test: any) {
    let testUrl =  "http://localhost:8080/test/submitTest";
		return this.http.post(testUrl, test, {headers: this.headers});
  }

  getCoursesByTeacher(){
    let testUrl =  "http://localhost:8080/test/getCoursesByTeacher";
		return this.http.get<any[]>(testUrl, {headers: this.headers});
  }
}
