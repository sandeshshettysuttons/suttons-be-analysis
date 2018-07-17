import { Injectable } from "@angular/core";
import { LocalStorageService } from "angular-2-local-storage";

import { Constants}   from "../common/constants";
import {Role} from "../model/role.model";

@Injectable()
export class DepartmentRoleService{

  constructor(private _localSt: LocalStorageService) {
  }

  getRoles(userId) : Role[]{
    return this._localSt.get(Constants.USER_ROLES);
  }

  setRoles(userId, templates){
    var userRoles = [];

    for(let template of templates){
      for(let role of template.roles){
        userRoles.push(role);
      }
    }

    this._localSt.add(Constants.USER_ROLES, userRoles);
  }

  getRoleDepartment() {
    return this._localSt.get(Constants.ROLE_DEPARTMENT);
  }

  setRoleDepartment(departmentCode){
    this._localSt.add(Constants.ROLE_DEPARTMENT, departmentCode);
  }

}
