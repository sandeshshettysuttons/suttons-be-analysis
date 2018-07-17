import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { DepartmentService }  from "../services/department.service";
import { MyprofileService }   from "../services/myprofile.service";
import { PermissionService }  from "../services/permission.service";

import { Department } from "../model/department.model";
import { Constants }  from "../common/constants";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  mainMenuItems =[];
  mainMenuItem = {title:"", routerLink:"", type:"", shortcut:""};
  userName="System";
  currentDepartment = new Department;
  departments = [];

  permission = {
    canViewSetting: false,
    canViewPageOne : false,
    canViewPageTwo : false,
    canViewUser : false,
    canViewDepartment : false,
    canViewCompany : false,
    canViewMailRecipient : false,
  }

  constructor( private _router: Router,
               private _myprofileService: MyprofileService,
               private _departmentService: DepartmentService,
               private _permission: PermissionService) { }

  ngOnInit() {
    this.setUserName();
    this.setCurrentDepartment();
    this.setDepartments();
    this.setPermission();
    this.createMainMenu();
  }


  createSettingsMenuItem = function(title, routerLink, type?:string) {
    let menuItem = Object.assign({}, this.mainMenuItem);
    menuItem.title = title;
    menuItem.routerLink = routerLink;
    menuItem.type = type;

    return menuItem;
  };

  createMainMenu(){
    // if (this.permission.canViewPageOne) {
    //   let menuVehicle = this.createSettingsMenuItem("Page 1","pageOne");
    //   this.mainMenuItems.push(menuVehicle);
    // }
    // if (this.permission.canViewPageTwo) {
    //   let menuVehicle = this.createSettingsMenuItem("Page 2","pageTwo");
    //   this.mainMenuItems.push(menuVehicle);
    // }
  }

  setUserName() {
    let loginData = this._myprofileService.getLoggedInData();
    if (loginData) {
      this.userName = loginData.profile.name;
    }
  }

  setPermission() {
    //Main menu permissions
    this.permission.canViewPageOne =  this._permission.hasPermission(PermissionService.permissions.CanViewPageOne);
    this.permission.canViewPageTwo = this._permission.hasPermission(PermissionService.permissions.CanViewPageTwo);

    //Settings menu permissions
    this.permission.canViewUser = this.setSettingsPermission(PermissionService.permissions.CanViewUser);
    this.permission.canViewDepartment = this.setSettingsPermission(PermissionService.permissions.CanViewDepartment);

    this.permission.canViewCompany = this.setSettingsPermission(PermissionService.permissions.CanViewCompany);
    this.permission.canViewMailRecipient = this.setSettingsPermission(PermissionService.permissions.CanViewMailRecipient);
  }

  setSettingsPermission(permission){
    var hasPermission = this._permission.hasPermission(permission);
    this.permission.canViewSetting = hasPermission ? true : this.permission.canViewSetting;
    return hasPermission
  }

  setCurrentDepartment() {
   this.currentDepartment = this._departmentService.getCurrentDepartment();
  }

  setDepartments() {
    let loginData = this._myprofileService.getLoggedInData();
    if (loginData) {
      this.departments = loginData.profile.departments;
    }
  }

  selectDepartment(department) {
    this.currentDepartment = department;
    this._departmentService.setCurrentDepartment(department);
    window.location.href = Constants.BASE_URI
  }
}
