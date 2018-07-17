import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';

import { AppError } from "../../../common/app-error";
import { NotFoundError } from "../../../common/not-found-error";

import { DepartmentService } from "../../../services/department.service";
import { LocalStorageService } from "angular-2-local-storage";
import { PermissionService } from "../../../services/permission.service";

import { Department } from "../../../model/department.model";
import { Constants } from "../../../common/constants";

@Component({
  selector: 'app-department-form',
  templateUrl: './department-form.component.html',
  styleUrls: ['./department-form.component.css']
})
export class DepartmentFormComponent implements OnInit {

  departmentForm: NgForm;
  @ViewChild('departmentForm') currentForm: NgForm;

  pageTitle= "View Department";
  editMode = false;

  department = new Department();

  errors = [];
  errorHappend = false;

  constructor(private _route: ActivatedRoute,
              private _location: Location,
              private _departmentService: DepartmentService,
              private _storage: LocalStorageService,
              private _permission: PermissionService) { }

  ngOnInit() {

    var id = this._route.params.subscribe(params => {

      var id = +params["id"];

      if(id && this.canEditDepartment()){
        this.pageTitle = "Edit Company";
        this.editMode = true;
      }else if(!id && this.canAddDepartment()){
        this.pageTitle = "Add Company";
        this.editMode = true;
      }

      if(!id){
        this.department.isActive = true;
        return;
      }

      this._departmentService.getById(id)
        .subscribe(
          department => {
            this.department = department.entity;
          },
          (error: AppError) => {
            if (error instanceof NotFoundError){
              alert('Customer details not found.');
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

  back() {
    this._location.back();
  }

  save() {

    this.onValueChanged(true);
    if (!this.departmentForm.valid) {
      return;
    }

    this.resetErrorMessages();

    this.department.f = Department.updateColumns;

    var result;
    if(this.department.id){
      result = this._departmentService.update(this.department, true);
    }else{
      result = this._departmentService.create(this.department, true);
    }

    result.subscribe(
      department => {
        this.back();
      },
      (error: AppError) => {
        this.errors = [error.message];
        this.errorHappend = true;
      }
    )
  }

  canViewDepartment() {
    return this._permission.hasPermission(PermissionService.permissions.CanViewDepartment);
  }

  canAddDepartment(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddDepartment);
  }

  canEditDepartment(){
    return this._permission.hasPermission(PermissionService.permissions.CanEditDepartment);
  }

  resetErrorMessages(){
    this.errors = [];
    this.errorHappend = false;
  }

  ngAfterViewChecked() {
    this.formChanged();
  }

  formChanged() {
    if (this.currentForm === this.departmentForm) { return; }
    this.departmentForm = this.currentForm;
    if (this.departmentForm) {
      this.departmentForm.valueChanges
        .subscribe(data => this.onValueChanged(false, data));
    }
  }

  onValueChanged(ignoreDirty: boolean, data?: any) {
    if (!this.departmentForm) { return; }
    const form = this.departmentForm.form;

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
    'code': '',
    'name': ''
  };

  validationMessages = {
    'code': {
      'required':   'Code is required.',
      'maxlength':  'Code cannot be more than 20 characters long.'
    },
    'name':  {
      'required':   'Name is required.'
    }
  };

}
