import { Component, OnInit } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

export interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}

export const ROUTES_TEACHER: RouteInfo[] = [
    { path: '/new-test',     title: 'Add new test',         icon:'nc-simple-add',       class: '' },
    { path: '/courses',     title: 'Courses',         icon:'nc-single-copy-04',       class: '' },
];

export const ROUTES_STUDENT: RouteInfo[] = [
    { path: '/courses',     title: 'Courses',         icon:'nc-single-copy-04',       class: '' },
];

@Component({
    moduleId: module.id,
    selector: 'sidebar-cmp',
    templateUrl: 'sidebar.component.html',
})

export class SidebarComponent implements OnInit {
    public menuItems: any[];
    ngOnInit() {
        if (this.checkRole() == "ROLE_TEACHER"){
            this.menuItems = ROUTES_TEACHER.filter(menuItem => menuItem);
        }
        else if (this.checkRole() == "ROLE_STUDENT") {
            this.menuItems = ROUTES_STUDENT.filter(menuItem => menuItem);
        }
        
    }

    checkRole(){
		const token = localStorage.getItem('user');
		const jwt: JwtHelperService = new JwtHelperService();
		let info = jwt.decodeToken(token)
		return info.role
    }
    
}

