import { Component, OnInit }  from '@angular/core';
import { PaginationData }     from '../../../model/pagination.model';
import { AppError }           from '../../../common/app-error';

import { Department }         from "../../../model/department.model";
import { DepartmentService }  from "../../../services/department.service";
import { PermissionService }  from "../../../services/permission.service";

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.css']
})
export class DepartmentListComponent implements OnInit {

  departments: Department[];
  pagination: PaginationData;

  searchCriteriaMaster = {
    typeahead: undefined,
    isActive: false
  };

  searchCriteria: object;

  orderBy = "code,name";
  reverse = false;

  showSearchPanel = false;
  searching = false;

  constructor(private _departmentService: DepartmentService, private _permission: PermissionService) { }

  ngOnInit() {
    this.resetCriteria();
    this.search();
  }

  canAddDepartment(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddDepartment);
  }

  search() {
    this.searching = true;

    this._departmentService.queryBySearchCriteria(this.searchCriteria, this.orderBy, this.reverse)
      .subscribe(departments => {
          this.departments = departments.entities;
          this.pagination = departments.pagination;

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
      this._departmentService.getByURL(this.pagination.next)
        .subscribe(departments => {
            for(let department of departments.entities){
              this.departments.push(department);
            }
            this.pagination = departments.pagination;

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
