/**
 * Created by nirmala.batuwitage on 07/06/2018.
 */

import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

import { Role } from "../model/role.model";
import { User } from "../model/user.model";

import { DepartmentRoleService } from "./department-role.service";
import { MyprofileService } from "./myprofile.service";

@Injectable()
export class PermissionService{

  userProfile: User;
  userRoles: Role[];

  static permissions = {
    CanViewPageOne: "PAGEONE_VIEW",
    CanViewPageTwo: "PAGETWO_VIEW",
    CanViewDepartment:  "DEPARTMENT_VIEW",
    CanAddDepartment:   "DEPARTMENT_ADD",
    CanEditDepartment:  "DEPARTMENT_EDIT",
    CanViewUser: "USER_VIEW",
    CanAddUser: "USER_ADD",
    CanEditUser: "USER_EDIT",

    CanViewCompany:  "COMPANY_VIEW",
    CanAddCompany:   "COMPANY_ADD",
    CanEditCompany:  "COMPANY_EDIT",
    CanViewMailRecipient:  "MAILRECIPIENT_VIEW",
    CanAddMailRecipient:   "MAILRECIPIENT_ADD",
    CanEditMailRecipient:  "MAILRECIPIENT_EDIT",
  }

  constructor(private _myprofile: MyprofileService,
              private _deptRoleService: DepartmentRoleService,
              private router: Router) {
  }

  hasPermission = function(permission, redirectOnFail?: boolean) {
    var hasPermission = false;
    this.loadUserProfile();

    if (this.userProfile) {
      if (this.userProfile.isSystemAdmin) {
        hasPermission = true;
      }else {
        this.loadUserRoles();
        if (this.userRoles) {
          for(var i = 0; i < this.userRoles.length; i++){
            if(this.userRoles[i].name == permission){
              hasPermission = true;
              break;
            }
          }
        }
      }
    }

    if(!hasPermission && redirectOnFail) {
      this.router.navigate(['access-denied']);
    }

    return hasPermission;
  }

  loadUserProfile(){
    if(!this.userProfile){
      this.userProfile = this._myprofile.getLoggedInProfile();
    }
  }

  loadUserRoles(){
    if(!this.userRoles){
      this.userRoles = this._deptRoleService.getRoles(this.userProfile.id);
    }
  }

}
