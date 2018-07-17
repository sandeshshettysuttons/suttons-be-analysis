import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Location } from '@angular/common';

import { AppError } from "../../../common/app-error";
import { NotFoundError } from "../../../common/not-found-error";

import { MailRecipientService } from "../../../services/mailRecipient.service";
import { LocalStorageService } from "angular-2-local-storage";
import { PermissionService } from "../../../services/permission.service";

import { MailRecipient } from "../../../model/mailRecipient.model";
import { Company } from "../../../model/company.model";
import { Constants } from "../../../common/constants";
import {Lookup} from "../../../model/lookup.model";
import {LookupService} from "../../../services/lookup.service";
import {CompanyService} from "../../../services/company.service";

@Component({
  selector: 'app-mailRecipient-form',
  templateUrl: './mailRecipient-form.component.html',
  styleUrls: ['./mailRecipient-form.component.css']
})
export class MailRecipientFormComponent implements OnInit {

  mailRecipientForm: NgForm;
  @ViewChild('mailRecipientForm') currentForm: NgForm;

  pageTitle= "View Mail Recipient";
  editMode = false;

  mailRecipient = new MailRecipient();

  errors = [];
  errorHappend = false;

  typeList : Lookup[];
  companyList = [];
  statusList : Lookup[];

  constructor(private _route: ActivatedRoute,
              private _location: Location,
              private _mailRecipientService: MailRecipientService,
              private _companyService: CompanyService,
              private _lookupService : LookupService,
              private _storage: LocalStorageService,
              private _permission: PermissionService) { }

  ngOnInit() {

    this.loadPageData();

    var id = this._route.params.subscribe(params => {

      var id = +params["id"];

      if(id && this.canEditMailRecipient()){
        this.pageTitle = "Edit Mail Recipient";
        this.editMode = true;
      }else if(!id && this.canAddMailRecipient()){
        this.pageTitle = "Add Mail Recipient";
        this.editMode = true;
      }

      this.mailRecipient.company = new Company();

      if(!id){
        this.mailRecipient.status = 'ACTIVE';
        return;
      }

      this._mailRecipientService.getById(id)
        .subscribe(
          mailRecipient => {
            this.mailRecipient = mailRecipient.entity;

            if (!this.mailRecipient.company) {
              this.mailRecipient.company = new Company();
            }
          },
          (error: AppError) => {
            if (error instanceof NotFoundError){
              alert('MailRecipient details not found.');
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

    this._companyService.queryBySearchCriteria({status:'ACTIVE'}, '', false)
      .subscribe(companies => {
          this.companyList = companies.entities;
        },
        (error: AppError) => {

        });

    this._lookupService.queryByType(Constants.MAILRECIPIENT_LOOKUP_TYPE_STATUS)
      .subscribe(statusList => {
          this.statusList = statusList.entities;
        },
        (error: AppError) => {
        });

    this._lookupService.queryByType(Constants.MAILRECIPIENT_LOOKUP_TYPE)
      .subscribe(typeList => {
          this.typeList = typeList.entities;
        },
        (error: AppError) => {
        });
  }

  back() {
    this._location.back();
  }

  save() {

    this.onValueChanged(true);
    if (!this.mailRecipientForm.valid) {
      return;
    }

    this.resetErrorMessages();

    if (this.mailRecipient.type == 'GLOBAL') {
      this.mailRecipient.company = null;
    }

    this.mailRecipient.f = MailRecipient.updateColumns;

    var result;
    if(this.mailRecipient.id){
      result = this._mailRecipientService.update(this.mailRecipient, true);
    }else{
      result = this._mailRecipientService.create(this.mailRecipient, true);
    }

    result.subscribe(
      mailRecipient => {
        this.back();
      },
      (error: AppError) => {
        this.errors = [error.message];
        this.errorHappend = true;
      }
    )
  }

  canViewMailRecipient() {
    return this._permission.hasPermission(PermissionService.permissions.CanViewMailRecipient);
  }

  canAddMailRecipient(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddMailRecipient);
  }

  canEditMailRecipient(){
    return this._permission.hasPermission(PermissionService.permissions.CanEditMailRecipient);
  }

  resetErrorMessages(){
    this.errors = [];
    this.errorHappend = false;
  }

  ngAfterViewChecked() {
    this.formChanged();
  }

  formChanged() {
    if (this.currentForm === this.mailRecipientForm) { return; }
    this.mailRecipientForm = this.currentForm;
    if (this.mailRecipientForm) {
      this.mailRecipientForm.valueChanges
        .subscribe(data => this.onValueChanged(false, data));
    }
  }

  onValueChanged(ignoreDirty: boolean, data?: any) {
    if (!this.mailRecipientForm) { return; }
    const form = this.mailRecipientForm.form;

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
    'email': '',
    'type': '',
    'status': '',
    'company': '',
  };

  validationMessages = {
    'name':  {
      'required':   'Required'
    },
    'email':  {
      'required':   'Required',
      'email':  'Wrong format'
    },
    'type':  {
      'required':   'Required'
    },
    'status':  {
      'required':   'Required'
    },
    'company':  {
      'required':   'Required'
    }
  };

}
