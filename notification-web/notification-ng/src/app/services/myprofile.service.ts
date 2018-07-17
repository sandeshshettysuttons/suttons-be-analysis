import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

import { Constants }  from "../common/constants";
import { MyProfile }  from "../model/myprofile.model";

@Injectable()
export class MyprofileService extends ApiService{
  resourceApiUri = "/myProfile";

  constructor(http: HttpClient, public _localSt: LocalStorageService) {
    super('/myProfile', http, _localSt);
  }

  getProfile(){
    return this.get(
      this.resourceApiUri,
      'user(*)+department(code,name)',
      null,
      null,
      false,
      1,
      true);
  }

  getRolesByDepartment(departmentCode){
    return this.get(
      this.resourceApiUri+'/template',
      'role(name)+template(name)',
      'departmentCode('+departmentCode+')',
      null,
      false,
      1,
      true);
  }

  getLoggedInData() : MyProfile {
    return this._localSt.get(Constants.LOGIN_DATA);
  }

  setLoggedInData(profile){
    this._localSt.add(Constants.LOGIN_DATA, profile);
  }

  getLoggedInProfile() {
    return this.getLoggedInData().profile;
  }

}
