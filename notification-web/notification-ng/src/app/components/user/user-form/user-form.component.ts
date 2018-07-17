import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';

import { AppError } from "../../../common/app-error";
import { NotFoundError } from "../../../common/not-found-error";

import { UserService } from "../../../services/user.service";
import { PermissionService } from "../../../services/permission.service";

import { User } from "../../../model/user.model";
import {TemplateService} from "../../../services/template.service";
import {Department} from "../../../model/department.model";
import {Template} from "../../../model/template.model";
import {UserAccess} from "../../../model/useraccess.model";
import {LocalStorageService} from "angular-2-local-storage";
import {Constants} from "../../../common/constants";
import {MyprofileService} from "../../../services/myprofile.service";
import {Lookup} from "../../../model/lookup.model";
import {LookupService} from "../../../services/lookup.service";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  userForm: NgForm;
  @ViewChild('userForm') currentForm: NgForm;

  pageTitle= "View User";
  editMode = false;

  user = new User();

  errors = [];
  errorHappend = false;

  setPin = false;

  templates = [];
  template : Template;
  departments = [];
  department : Department;

  statusList : Lookup[];

  constructor(private _route: ActivatedRoute,
              private _location: Location,
              private _userService: UserService,
              private _templateService: TemplateService,
              private _lookupService : LookupService,
              private _storage: LocalStorageService,
              private _myprofileService: MyprofileService,
              private _permission: PermissionService) { }

  ngOnInit() {

    this.loadPageData();

    var id = this._route.params.subscribe(params => {

      var id = +params["id"];

      if(id && this.canEditUser()){
        this.pageTitle = "Edit User";
        this.editMode = true;
      }else if(!id && this.canAddUser()){
        this.pageTitle = "Add User";
        this.editMode = true;
      }

      if(!id){

        this.user.userAccesses = [];
        this.user.templates = [];
        this.user.departments = [];
        // this.user.isStaff = false;

        // Make all users system admin by default
        this.user.isSystemAdmin = true;

        return;
      }

      this._userService.getById(id)
        .subscribe(
          user => {
            this.user = user.entity;

            this.user.password=null;

            if (!this.user.userAccesses) {
              this.user.userAccesses = [];
            } else {
              this.loadUserAccesses();
            }

          },
          (error: AppError) => {
            if (error instanceof NotFoundError){
              alert('User details not found.');
            }
            else {
              console.log(error.originalError.developerMessage);
              alert(error.message);
            };
            this.back();
          }
        )
    });
  }

  loadPageData(){

    this.departments=this._myprofileService.getLoggedInData().profile.departments;
    this.department = this._storage.get(Constants.CURRENT_DEPARTMENT);

    this._templateService.queryBySearchCriteria({}, '', false)
      .subscribe(templates => {
          this.templates = templates.entities;
        },
        (error: AppError) => {

        });

    this._lookupService.queryByType(Constants.USER_LOOKUP_TYPE_STATUS)
      .subscribe(statusList => {
          this.statusList = statusList.entities;
        },
        (error: AppError) => {
        });
  }

  back() {
    this._location.back();
  }

  save() {

    this.onValueChanged(true);

    if (!this.userForm.valid) {
      return;
    }

    this.resetErrorMessages();

    this.user.f = User.updateColumns;

    if (!this.setPin) {
      this.user.password=null;
    }

    var result;
    if(this.user.id){
      result = this._userService.update(this.user);
    }else{
      result = this._userService.create(this.user);
    }

    result.subscribe(
      user => {
        this.back();
      },
      (error: AppError) => {
        this.errors = [error.message];
        this.errorHappend = true;
      }
    )
  }

  canViewUser() {
    return this._permission.hasPermission(PermissionService.permissions.CanViewUser);
  }

  canAddUser(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddUser);
  }

  canEditUser(){
    return this._permission.hasPermission(PermissionService.permissions.CanEditUser);
  }

  resetErrorMessages(){
    this.errors = [];
    this.errorHappend = false;
  }

  ngAfterViewChecked() {
    this.formChanged();
  }

  formChanged() {
    if (this.currentForm === this.userForm) { return; }
    this.userForm = this.currentForm;
    if (this.userForm) {
      this.userForm.valueChanges
        .subscribe(data => this.onValueChanged(false, data));
    }
  }

  onValueChanged(ignoreDirty: boolean, data?: any) {
    if (!this.userForm) { return; }
    const form = this.userForm.form;

    for (const field in this.formErrors) {
      // clear previous error message (if any)
      this.formErrors[field] = '';
      const control = form.get(field);

      if (control && (ignoreDirty || control.dirty) && !control.valid) {
        const messages = this.validationMessages[field];
        for (const key in control.errors) {
          this.formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }

  formErrors = {
    'firstName': '',
    'lastName': '',
    'email': '',
    'phoneNumber': '',
    'status': '',
    'userName': ''
  };

  validationMessages = {
    'firstName': {
      'required':   'Required.'
    },
    'lastName':  {
      'required':   'Required.'
    },
    'email':  {
      'required':   'Required.',
      'email'   :   'Wrong format.'
    },
    'phoneNumber':  {
      'required':   'Required.'
    },
    'status':  {
      'required':   'Required.'
    },
    'userName':  {
      'required':   'Required.'
    },
  };

  addUserAccess() {
    if (this.template && this.department) {

      if (this.checkUserAccessExists()) {
        alert("The template is already selected for this department");

      } else {

        var userAccess = new UserAccess();
        userAccess.department=this.department;
        userAccess.template=this.template;
        this.user.userAccesses.push(userAccess);
      }
    }
  }

  checkUserAccessExists() : boolean {

    for (const userAccess of this.user.userAccesses) {

      if (userAccess.department.id == this.department.id
        && userAccess.template.id == this.template.id) {
        return true;
      }

    }

    return false;
  }

  removeUserAccess(userAccessToRemove : UserAccess) {
    this.user.userAccesses.splice(this.user.userAccesses.indexOf(userAccessToRemove), 1);
  }

  loadUserAccesses() {

    if (this.user.userAccesses && this.user.departments && this.user.templates
      && this.user.userAccesses.length == this.user.departments.length
      && this.user.userAccesses.length == this.user.templates.length)  {

      let i=0;

      for (let userAccess of this.user.userAccesses) {

        userAccess.department = this.user.departments[i];
        userAccess.template = this.user.templates[i];
        i++;
      }

    }

  }

  validateUserAccessesOnSave() : boolean {

    if (this.user.userAccesses && this.user.userAccesses.length > 0) {
      return true;
    }

    alert("You must add atleast one template access to save");
    return false;
  }

  showUserAccess(userAccess : UserAccess) : boolean {

    for (const dept of this.departments) {

      if (dept.id == userAccess.department.id) {
        return true;
      }

    }

    return false;
  }

}
