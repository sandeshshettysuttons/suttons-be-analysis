import { Component, OnInit }  from '@angular/core';
import { PaginationData }     from '../../../model/pagination.model';
import { AppError }           from '../../../common/app-error';

import { MailRecipient }         from "../../../model/mailRecipient.model";
import { MailRecipientService }  from "../../../services/mailRecipient.service";
import { PermissionService }  from "../../../services/permission.service";
import {CompanyService} from "../../../services/company.service";
import {LookupService} from "../../../services/lookup.service";
import {Constants} from "../../../common/constants";
import {Lookup} from "../../../model/lookup.model";

@Component({
  selector: 'app-mailRecipient-list',
  templateUrl: './mailRecipient-list.component.html',
  styleUrls: ['./mailRecipient-list.component.css']
})
export class MailRecipientListComponent implements OnInit {

  mailRecipients: MailRecipient[];
  pagination: PaginationData;

  searchCriteriaMaster = {
    name: undefined,
    status: 'ACTIVE',
    company:""
  };

  searchCriteria: object;

  orderBy = "name";
  reverse = false;

  showSearchPanel = false;
  searching = false;

  companyList = [];
  statusList : Lookup[];

  constructor(private _mailRecipientService: MailRecipientService,
              private _permission: PermissionService,
              private _companyService: CompanyService,
              private _lookupService : LookupService) { }

  ngOnInit() {
    this.resetCriteria();
    this.search();
    this.loadPageData();
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
  }

  canAddMailRecipient(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddMailRecipient);
  }

  search() {
    this.searching = true;

    this._mailRecipientService.queryBySearchCriteria(this.searchCriteria, this.orderBy, this.reverse)
      .subscribe(mailRecipients => {
          this.mailRecipients = mailRecipients.entities;
          this.pagination = mailRecipients.pagination;

          this.searching = false;
          this.showSearchPanel = false;
        },
        (error: AppError) => {
          this.searching = false;
        });
  };

  displayMoreItems(){
    if(this.searching){
      return;
    }

    if(this.pagination && this.pagination.next){
      this.searching = true;
      this._mailRecipientService.getByURL(this.pagination.next)
        .subscribe(mailRecipients => {
            for(let mailRecipient of mailRecipients.entities){
              this.mailRecipients.push(mailRecipient);
            }
            this.pagination = mailRecipients.pagination;

            this.searching = false;
          },
          (error: AppError) => {
            this.searching = false;
          });
    }
  }

  newSearch() {
    this.showSearchPanel = true;
  };

  cancelSearch() {
    this.showSearchPanel = false;
  };

  resetCriteria() {
    this.searchCriteria = Object.assign({}, this.searchCriteriaMaster);
  };

  order(_orderBy, _reverse) {
    this.orderBy = _orderBy;
    this.reverse = _reverse;
    this.search();
  };


}
