import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthenticationService } from 'app/services/auth.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';
import * as vkbeautify from 'vkbeautify';
import { Pipe, PipeTransform } from "@angular/core";
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-test-preview',
  templateUrl: './test-preview.component.html',
  styleUrls: ['./test-preview.component.css']
})

export class TestPreviewComponent  implements OnInit {
 
  private error = null;
  private isTeacher : boolean= false;
  private done : boolean= false;
  private courseId:number;
  private testsToDo : Array<any> = [];
  private xml: any;
  constructor(private route: ActivatedRoute,
    private router: Router, 
    private testService: TestService, 
    private toastr: ToastrService,
    private authService: AuthenticationService,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.courseId = params['id']);
    let link = this.route.toString();
    console.log(link);
    if(link.includes('Done')){
      this.getTestsDone();
      this.done = true;
    }
    else if(this.authService.isTeacher()){
      this.getTeachersTests();
      this.isTeacher = true;
      console.log("TEACHEEEER");
    }else{
      this.getTestsToDo();
    }
  }

  getTeachersTests() : void{
    this.testService.getTeacherCourseTests(this.courseId).subscribe(
      tests => {
        this.testsToDo = tests;
        console.log(this.testsToDo);
        this.error = null;
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }

  getTestsToDo() : void{
    this.testService.getTestsToDoByCourse(this.courseId).subscribe(
      tests => {
        this.testsToDo = tests;
        console.log(this.testsToDo);
        this.error = null;
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }

  getTestsDone():void{
    this.testService.getTestsDoneByCourse(this.courseId).subscribe(
      tests => {
        console.log("DONE")
        this.testsToDo = tests;
        console.log(tests);
        this.error = null;
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }
  startTest(id : number) : void{
    // window.location.href = `#/doneTestDetails/${id}`;
    // window.location.reload();
    this.router.navigate(['testInProgress', id]);
  }

  startDrivenTesting(id : number) : void {
    this.router.navigate(['drivenTestingInProgress', id]);
  }

  testDetails(id: number) : void{
    // window.location.href = `#/testInProgress/${id}`;
    // window.location.reload();
    this.router.navigate(['doneTestDetails', id]);
  }

  testDetailsTeacher(id: number) : void{
    // let url = "http://localhost:4200/testDetailsTeacher/1"
    // this.router.navigateByUrl(url)
    // this.router.navigate(['testDetailsTeacher', id]);
    // window.location.href = `#/testDetailsTeacher/${id}`;
    // window.location.reload();
    // this.router.navigate(['testDetailsTeacher', id]);
    window.location.href = `#/testDetailsTeacher/${id}`;
    window.location.reload();
  }

  exportToXml(id: number) : void {
    this.testService.exportImsQti(id).subscribe(
      result => {
      this.xml = result.node;
      console.log(this.xml)
      const dialogRef = this.dialog.open(XmlDisplay, {
        width: '700px',
        data: this.xml
      });
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }
}

@Component({
  templateUrl: 'xml-display.html'
})
@Pipe({
  name: 'xmlFormat'
})
export class XmlDisplay implements PipeTransform {
  
  transform(value: string): string {
    return vkbeautify.xml(value);
  }
  constructor(
    public dialogRef: MatDialogRef<XmlDisplay>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}