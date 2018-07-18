import { Component, OnInit }  from '@angular/core';
import { PaginationData }     from '../../../model/pagination.model';
import { AppError }           from '../../../common/app-error';

import { Company }         from "../../../model/company.model";
import { CompanyService }  from "../../../services/company.service";
import { PermissionService }  from "../../../services/permission.service";
import {Lookup} from "../../../model/lookup.model";
import {LookupService} from "../../../services/lookup.service";
import {Constants} from "../../../common/constants";

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrls: ['./company-list.component.css']
})
export class CompanyListComponent implements OnInit {

  companies: Company[];
  pagination: PaginationData;

  searchCriteriaMaster = {
    name: '',
    code: '',
    status: 'ACTIVE'
  };

  searchCriteria: object;

  orderBy = "name";
  reverse = false;

  showSearchPanel = false;
  searching = false;

  statusList : Lookup[];

  constructor(private _companyService: CompanyService,
              private _permission: PermissionService,
              private _lookupService : LookupService) { }

  ngOnInit() {
    this.resetCriteria();
    this.search();
    this.loadPageData();
  }

  loadPageData(){

    this._lookupService.queryByType(Constants.COMPANY_LOOKUP_TYPE_STATUS)
      .subscribe(statusList => {
          this.statusList = statusList.entities;
        },
        (error: AppError) => {
        });

  }

  canAddCompany(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddCompany);
  }

  search() {
    this.searching = true;

    this._companyService.queryBySearchCriteria(this.searchCriteria, this.orderBy, this.reverse)
      .subscribe(companies => {
          this.companies = companies.entities;
          this.pagination = companies.pagination;

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
      this._companyService.getByURL(this.pagination.next)
        .subscribe(companies => {
            for(let company of companies.entities){
              this.companies.push(company);
            }
            this.pagination = companies.pagination;

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
