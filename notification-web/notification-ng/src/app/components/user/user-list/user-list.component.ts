import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/user.model';
import { PaginationData } from '../../../model/pagination.model';
import { AppError } from '../../../common/app-error';
import { PermissionService } from "../../../services/permission.service";


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  pagination: PaginationData;

  searchCriteriaMaster = {
    typeahead: undefined,
    isActive: false
  };

  searchCriteria: object;

  orderBy = "firstName";
  reverse = false;

  showSearchPanel = false;
  searching = false;

  constructor(private _userService: UserService, private _permission: PermissionService) { }

  ngOnInit() {
    this.resetCriteria();
    this.search();
  }

  canAddUser(){
    return this._permission.hasPermission(PermissionService.permissions.CanAddUser);
  }

  search() {
    this.searching = true;

    this._userService.queryBySearchCriteria(this.searchCriteria, this.orderBy, this.reverse)
      .subscribe(users => {
          this.users = users.entities;
          this.pagination = users.pagination;

          this.searching = false;
          this.showSearchPanel = false;
        },
        (error: AppError) => {
          this.searching = false;
        });
  };

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

  displayMoreItems(){
    if(this.searching){
      return;
    }

    if(this.pagination && this.pagination.next){
      this.searching = true;

      this._userService.getByURL(this.pagination.next)
        .subscribe(users => {
            for(let user of users.entities){
              this.users.push(user);
            }
            this.pagination = users.pagination;

            this.searching = false;

          },
          (error: AppError) => {
            this.searching = false;
          });


    }
  }

}
