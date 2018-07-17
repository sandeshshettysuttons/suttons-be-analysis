import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

import { Constants }  from "../common/constants";
import { Department } from "../model/department.model";
import {DepartmentRoleService} from "./department-role.service";
import {AppError} from "../common/app-error";
import {MyprofileService} from "./myprofile.service";
import {MyProfile} from "../model/myprofile.model";

@Injectable()
export class DepartmentService extends ApiService{

  resourceApiUri = "/departments";

  constructor(http: HttpClient,
              public _localSt: LocalStorageService,
              private _deptRoleService: DepartmentRoleService,
              private _myprofileService: MyprofileService) {
    super('/departments', http, _localSt);
  }

  getCurrentDepartment() : Department{
    return this._localSt.get(Constants.CURRENT_DEPARTMENT)
  }

  setCurrentDepartment(department){
    this._localSt.add(Constants.CURRENT_DEPARTMENT, department);

    var myProfile = this._myprofileService.getLoggedInData();

    if (department.code && ( !myProfile.profile.isSystemAdmin
        || department.code != this._deptRoleService.getRoleDepartment())){

      myProfile.profile.isStaffForCurrentDepartment = false;
      //set isStaff flag for new department
      if (myProfile.profile.userAccesses) {
        for(let userAccess of myProfile.profile.userAccesses){
          if(userAccess.departmentId == department.id && userAccess.isStaff){
            myProfile.profile.isStaffForCurrentDepartment = true;
            break;
          }
        }
      }
      this._myprofileService.setLoggedInData(myProfile);

      //set roles for new department
      this._myprofileService.getRolesByDepartment(department.code)
        .subscribe(roles => {
            this._deptRoleService.setRoles(myProfile.profile.id, roles.entities);
            this._deptRoleService.setRoleDepartment(department.code);
          },
          (error: AppError) => {
          });

    }
  }

  queryBySearchCriteria(searchCriteria, _orderBy: string, _reverse: boolean){

    let _queryParam = '';
    let firstCriteria = true;

    if (searchCriteria) {

      if (searchCriteria.typeahead) {
        _queryParam += (firstCriteria ? '': '+')+'typeahead('+searchCriteria.typeahead+')';
        firstCriteria = false;
      }
      if (searchCriteria.code) {
        _queryParam += (firstCriteria ? '': '+')+'code('+searchCriteria.code+')';
        firstCriteria = false;
      }
      if (searchCriteria.name) {
        _queryParam += (firstCriteria ? '': '+')+'name('+searchCriteria.name+')';
        firstCriteria = false;
      }
      if (searchCriteria.isActive) {
        _queryParam += (firstCriteria ? '': '+')+'isActive('+searchCriteria.isActive+')';
        firstCriteria = false;
      }
    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'department(code,name,isActive)', _queryParam, _orderBy, _reverse, null, true);
  }

  getById(id){
    let newUrl = this.resourceApiUri + '/' + id;
    return this.get(newUrl, 'department(version,code,name,isActive)', null, null, null, null, true);
  }

}
