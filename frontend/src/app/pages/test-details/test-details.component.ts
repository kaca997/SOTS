import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthenticationService } from 'app/services/auth.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-test-details',
  templateUrl: './test-details.component.html',
  styleUrls: ['./test-details.component.css'],
})
export class TestDetailsComponent implements OnInit {

  private error = null;
  private isTeacher = false;
  private done : boolean= false;
  private testId:number;
  private test:any = {};
  private pageNumber: number = 0;
  constructor(private route: ActivatedRoute,
    private router: Router, 
    private testService: TestService,
    private toastr: ToastrService,
    private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.testId = params['id']);
    let link = this.route.toString();
    if(link.includes('done')){
      this.done = true;
      this.getTestDone();
    }
    else if(this.authService.isTeacher()){
      this.isTeacher = true;
      this.getTestTeacher();
      console.log("TEACHER")
    }
    else{
      console.log("STUDENT")
      this.getTestToDo();
    }
  }

  getTestToDo() : void{
    this.testService.getTest(this.testId).subscribe(
      test => {
        this.test = test;
        console.log(this.test);
        this.error = null;
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }

  getTestTeacher() : void{
    this.testService.getTestTeacher(this.testId).subscribe(
      test => {
        this.test = test;
        this.error = null;
        console.log(this.test);
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
        this.error = error.error;
        ;
        //this.router.navigate(['error-page'])
      }
    );
  }

  getTestDone() : void{
    this.testService.getDoneTest(this.testId).subscribe(
      test => {
        this.test = test;
        this.error = null;
        console.log(this.test);
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }

  setPage(i : number): void{
    this.pageNumber=i;
  }

  previous(): void{
    this.pageNumber--;
  }

  next(): void{
    this.pageNumber++;
  }

  submitTest(){
    this.testService.submitTest(this.test).subscribe(
      test => {
        this.toastr.success("Test submited! See your results!");
        this.test = test;
        console.log(test)
        this.router.navigate(['doneTestDetails', this.test.doneTestId]);
        this.pageNumber = 0;
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }
}
