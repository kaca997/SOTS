import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators, AbstractControl  } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from 'app/services/course.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-new-test',
  templateUrl: './new-test.component.html',
  styleUrls: ['./new-test.component.css']
})
export class NewTestComponent implements OnInit {
  form : FormGroup;
  testForm : FormGroup;
  sectionForm : FormGroup;
  questions: any[] = []
  courses: any[] = []
  sections: any[] = []
  problems: any[] = []
  questionNum = 1
  answerNum = 1
  sectionNum = 1
  // courseId: number;

  constructor(private fb: FormBuilder,
    private router: Router,
    private testService: TestService,
    private courseService: CourseService,
    private toastr: ToastrService ) { 

      this.form = this.fb.group({
        question :[null, Validators.required],
        problem :[null, Validators.required],
        firstAnswer :[null, Validators.required], 
        firstCorr : [false, Validators.required],
        formAnswers: this.fb.array([]),
        //corr: [false] a
      });

      this.sectionForm = this.fb.group({
        section : [null, Validators.required]
      });

      this.testForm = this.fb.group({
        testName: [null, Validators.required],
        course : [null, Validators.required]
      })
    }

  ngOnInit(): void {
    this.getCourses();
  }

  finishTest(){
    if (this.validationOnCLick()) {
      return
    }
    let answers: any[] = []
    answers.push({
      text: this.form.controls['firstAnswer'].value,
      correct: this.form.controls['firstCorr'].value
     })
    answers = answers.concat(this.form.controls['formAnswers'].value)
    let flag = false;
    if (answers.some(code => code.correct === true)){
      flag = true;
    }

    if (flag == false){
      this.toastr.error("There mustn't be questions that do not have correct answer.");
      return;
    }
    console.log(answers)
    this.questions.push({
      text: this.form.value.question,
      answers: answers,
      problem : {
        id: this.form.value.problem,
        }
      }
    )
    
    this.sections.push({
      sectionTitle : this.sectionForm.controls['section'].value,
      questions : this.questions
    })
    this.form.reset();
    this.form.get("problem").setErrors(null) ;
    this.questionNum = 1;
    this.sectionNum = 1;
    const control = <FormArray>this.form.controls['formAnswers'];
    for(let i = control.length-1; i >= 0; i--) {
        control.removeAt(i)
    }   
    this.form.controls['firstCorr'].setValue(false)
    this.questions = []
    this.sectionForm.reset();

    let test = {
      testTitle : this.testForm.controls['testName'].value,
      courseId: this.testForm.controls['course'].value,
      sections: this.sections
    }
    this.testForm.reset();
    console.log(test);
    this.sections = []

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

  getCourses(){
    this.testService.getCoursesByTeacher().subscribe(
      courses => {
        this.courses = courses;
        console.log(courses);
      },
      error => {
        console.log(error);
      }
    )
  }

  onChange(event){
    this.problems = []
    if (event == null) {
      return
    }
    this.courseService.getProblemsByCourse(event).subscribe(
      problems => {
        this.problems = problems;
        console.log(problems);
      },
      error => {
        this.toastr.error(error.error);
      }
    )
  }



  nextSection(){
    if (this.validationOnCLick()) {
      return
    }
    let answers: any[] = []
    answers.push({
      text: this.form.controls['firstAnswer'].value,
      correct: this.form.controls['firstCorr'].value
     })
    answers = answers.concat(this.form.controls['formAnswers'].value)
    console.log(answers)
    let flag = false;
    if (answers.some(code => code.correct === true)){
      flag = true;
    }

    if (flag == false){
      this.toastr.error("There mustn't be questions that do not have correct answer.");
      return;
    }
    this.questions.push({
      text: this.form.value.question,
      answers: answers,
      problem : {
        id: this.form.value.problem,
        }
      }
    )
    
    this.sections.push({
      sectionTitle : this.sectionForm.controls['section'].value,
      questions : this.questions
    })
    this.form.reset();
    this.form.get("problem").setErrors(null) ;
    this.questionNum = 1;
    this.sectionNum++;
    const control = <FormArray>this.form.controls['formAnswers'];
    for(let i = control.length-1; i >= 0; i--) {
        control.removeAt(i)
    }   
    this.form.controls['firstCorr'].setValue(false)
    this.questions = []
    this.sectionForm.reset();
  }

  newAnswer(): FormGroup {
  //  this.answerNum++;
    return this.fb.group({
      text: '',
      correct: false,
    })
    
  }

  formAnswers() : FormArray {
    return this.form.get("formAnswers") as FormArray
  }
  
  addAnswer() {
    if (this.validationOnCLick()) {
      return
    }
    this.formAnswers().push(this.newAnswer());
  }

  removeAnswer(i:number) {
    this.formAnswers().removeAt(i);
  }

  nextQuestion(){
    this.form.controls['problem'].setValidators([Validators.required])
    this.form.controls['problem'].updateValueAndValidity();
    if (this.validationOnCLick()) {
      return
    }
    let answers: any[] = []
    answers.push({
      text: this.form.controls['firstAnswer'].value,
      correct: this.form.controls['firstCorr'].value
     })
     
    answers = answers.concat(this.form.controls['formAnswers'].value)
    let flag = false;
     if (answers.some(code => code.correct === true)){
       flag = true;
     }
 
     if (flag == false){
       this.toastr.error("There mustn't be questions that do not have correct answer.");
       return;
     }
    console.log(answers)
    this.questions.push({
      text: this.form.value.question,
      answers: answers,
      problem : {
        id: this.form.value.problem,
        }
      }
    )

    this.form.reset();
    this.form.get("problem").setErrors(null) ;
  

    const control = <FormArray>this.form.controls['formAnswers'];
    for(let i = control.length-1; i >= 0; i--) {
        control.removeAt(i)
    }   
    this.questionNum++;
    this.form.controls['firstCorr'].setValue(false)
   
  }

  validationOnCLick(){
    if (this.testForm.invalid || this.sectionForm.invalid || this.form.invalid){
      this.testForm.markAllAsTouched();
      this.form.markAllAsTouched();
      this.sectionForm.markAllAsTouched();
      return true;
    }
    else{
      return false;
    }
  }

  onChangeP(event) {
  }
}
