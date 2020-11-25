import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-new-test',
  templateUrl: './new-test.component.html',
  styleUrls: ['./new-test.component.css']
})
export class NewTestComponent implements OnInit {
  form : FormGroup;
  answers: any[] = []
  questions = []

  constructor(private fb: FormBuilder,
    private router: Router,
    private testService: TestService,
    private toastr: ToastrService ) { 

      this.form = this.fb.group({
        question :[null, Validators.required],
        firstAnswer :[null, Validators.required], 
        firstCorr : [false, Validators.required],
        answer: [null],
        corr: [false] 
      })
    }

  ngOnInit(): void {
  }

  addNewAnswer() {
    if (this.answers.length == 0) {
      this.answers.push({
        text: this.form.value.firstAnswer,
        correct: this.form.value.firstCorr
      })
    }
    else{
      this.answers.push({
        text: this.form.value.answer,
        correct: this.form.value.corr
      })
      // this.form.controls['answer'].setValue("")
    }
    // console.log(this.answers)
  }

  nextQuestion(){
    this.addNewAnswer()
    
    this.questions.push({
      text: this.form.value.question,
      answers: this.answers
    }
    )
    this.form.reset();
    this.form.controls['firstCorr'].setValue(false)
    this.form.controls['corr'].setValue(false)
    console.log(this.questions)
    this.answers = []
  }

  finishTest(){
    this.nextQuestion();
    this.form.reset();
    let test = {
      questions: this.questions
    }
    console.log(JSON.stringify(test));
    this.testService.createTest(test).subscribe(
			result => {
        console.log(result)
        console.log(JSON.stringify(result));
        this.toastr.success('Successfully added.');
			},
			error => {
				console.log(error);
        		this.toastr.error(error.error);
			}
		);
  }

}
