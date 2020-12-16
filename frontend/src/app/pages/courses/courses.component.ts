import { Route } from '@angular/compiler/src/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'app/services/auth.service';
import { CourseService } from 'app/services/course.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {

  private isTeacher = false;
  courses :Array<any> = []
  constructor(private courseService: CourseService,
    private autService: AuthenticationService,
    private toastr: ToastrService,
    private router: Router,
    private testService: TestService) { }
  
    ngOnInit(): void {
      if(this.autService.isTeacher()){
        this.isTeacher = true;
      }
      this.getCourses();
  }

  getCourses(): any{
    let role = "Student";
    if(this.autService.getRole()=="ROLE_TEACHER")
      role = "Teacher"
    this.courseService.getAllCoursesForUser(role).subscribe(
      courses => {
        this.courses = courses;
        console.log(this.courses)
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }
  testsToDo(id:number): void{
    this.router.navigate(['testsToDo', id]);
  }

  doneTests(id:number): void{
    this.router.navigate(['testsDone', id]);
  }

  myTests(id:number): void{
    this.router.navigate(['teacherTests', id]);
  }
  
  showKnoledgeSpaces(id: number): void{
    this.router.navigate(['knowledge-spaces', id])
  }
}
