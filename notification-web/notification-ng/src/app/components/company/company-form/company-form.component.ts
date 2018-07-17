import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';

import { AppError } from "../../../common/app-error";
import { NotFoundError } from "../../../common/not-found-error";

import { CompanyService } from "../../../services/company.service";
import { LocalStorageService } from "angular-2-local-storage";
import { PermissionService } from "../../../services/permission.service";

import { Company } from "../../../model/company.model";
import { Constants } from "../../../common/constants";
import {LookupService} from "../../../services/lookup.service";
import {Lookup} from "../../../model/lookup.model";

@Component({
  selector: 'app-company-form',
  templateUrl: './company-form.component.html',
  styleUrls: ['./company-form.component.css']
})
export class CompanyFormComponent implements OnInit {

  companyForm: NgForm;
  @ViewChild('companyForm') currentForm: NgForm;

  pageTitle= "View Company";
  editMode = false;

  company = new Company();

  errors = [];
  errorHappend = false;

  statusList : Lookup[];

  constructor(private _route: ActivatedRoute,
              private _location: Location,
              private _companyService: CompanyService,
              private _lookupService : LookupService,
              private _storage: LocalStorageService,
              private _permission: PermissionService) { }

  ngOnInit() {

    this.loadPageData();

    var id = this._route.params.subscribe(params => {

      var id = +params["id"];

      if(id && this.canEditCompany()){
        this.pageTitle = "Edit Company";
        this.editMode = true;
      }else if(!id && this.canAddCompany()){
        this.pageTitle = "Add Company";
        this.editMode = true;
      }

      if(!id){
        this.company.status = "ACTIVE";
        return;
      }

      this._companyService.getById(id)
        .subscribe(
          company => {
            this.company = company.entity;
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

  loadPageData(){

    this._lookupService.queryByType(Constants.COMPANY_LOOKUP_TYPE_STATUS)
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
    if (!this.companyForm.valid) {
      return;
    }

    this.resetErrorMessages();

    this.company.f = Company.updateColumns;

    var result;
    if(this.company.id){
      result = this._companyService.update(this.company, true);
    }else{
      result = this._companyService.create(this.company, true);
    }

    result.subscribe(
      company => {
        this.back();
      },
      (error: AppError) => {
        this.errors = [error.message];
        this.errorHappend = true;
      }
    )
  }

  canViewCompany() {
    return this._permission.hasPermission(PermissionService.permissions.CanViewCompany);
  }

  canAddCompany(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddCompany);
  }

  canEditCompany(){
    return this._permission.hasPermission(PermissionService.permissions.CanEditCompany);
  }

  resetErrorMessages(){
    this.errors = [];
    this.errorHappend = false;
  }

  ngAfterViewChecked() {
    this.formChanged();
  }

  formChanged() {
    if (this.currentForm === this.companyForm) { return; }
    this.companyForm = this.currentForm;
    if (this.companyForm) {
      this.companyForm.valueChanges
        .subscribe(data => this.onValueChanged(false, data));
    }
  }

  onValueChanged(ignoreDirty: boolean, data?: any) {
    if (!this.companyForm) { return; }
    const form = this.companyForm.form;

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
    'name': '',
    'status': ''
  };

  validationMessages = {
    'name':  {
      'required':   'Name is required.'
    },
    'status':  {
      'required':   'Status is required.'
    }
  };

}
