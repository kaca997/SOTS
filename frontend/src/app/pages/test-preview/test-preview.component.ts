import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthenticationService } from 'app/services/auth.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';

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
  constructor(private route: ActivatedRoute,
    private router: Router, 
    private testService: TestService, 
    private toastr: ToastrService,
    private authService: AuthenticationService) { }

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
    this.router.navigate(['testInProgress', id]);
  }

  testDetails(id: number) : void{
    this.router.navigate(['doneTestDetails', id]);
  }

  testDetailsTeacher(id: number) : void{
    this.router.navigate(['testDetailsTeacher', id]);
  }
}