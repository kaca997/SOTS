import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DomainService {
  private headers = new HttpHeaders({ 'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Credentials': 'true'});
  
  constructor(private http: HttpClient) { }

  creteDomain(domainDTO: any): Observable<any> {
		let domainUrl =  "http://localhost:8080/domain/create";
		return this.http.post(domainUrl, {domainName: domainDTO.domainName, courseId: domainDTO.courseId, problemList: domainDTO.problemList, relations: domainDTO.relations}, {headers: this.headers, responseType: 'text'});
  }
  
  getDomain(id : number): Observable<any>{
    let testUrl = `http://localhost:8080/domain/get/${id}`
    return this.http.get(testUrl, {headers: this.headers});
  }
  
}
