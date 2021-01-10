import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-driven-testing',
  templateUrl: './driven-testing.component.html',
  styleUrls: ['./driven-testing.component.css']
})
export class DrivenTestingComponent implements OnInit {

  private testId: number;
  private kstateProb: any;
  private test: any;
  private error = null;
  private counter = 1;
  private finalState : any;
  private isFinalState : boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router, 
    private testService: TestService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.testId = params['id']);
    this.getKStateProb(this.testId);
    this.getTestToDo();
  }

  getKStateProb(id : number) {
    this.testService.getForDrivenTesting(this.testId).subscribe(
      res => {
        this.kstateProb = res;
        console.log(this.kstateProb);
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
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

  next(): void {
    this.kstateProb.questions.push(this.kstateProb.questionToAsk);
    console.log(this.kstateProb);
    this.submitQuestion(this.kstateProb);
  }

  submitQuestion(state : any) {
    this.testService.submitQuestion(state, this.testId).subscribe(
      res => {
        if (this.checkFinalState(res)) {
          return;
        }
        
        this.kstateProb = res;
        this.counter = this.kstateProb.questions.length + 1;
        console.log(this.kstateProb);
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }

  checkFinalState(state:any) {
    if (state.finalState != null) {
      let all = state.finalState.problems
      console.log(all)
      this.isFinalState = true;
      this.finalState = state.finalState;
      return true;
    }
    return false;
  }
}
