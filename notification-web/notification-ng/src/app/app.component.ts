import { Component, OnInit} from "@angular/core";

import { AppError }   from "./common/app-error";
import { Constants }  from "./common/constants";

import { Department } from "./model/department.model";
import { MyProfile }  from "./model/myprofile.model";

import { DepartmentRoleService }  from "./services/department-role.service";
import { DepartmentService }      from "./services/department.service";
import { MyprofileService }       from "./services/myprofile.service";
import { LocalStorageService }    from "angular-2-local-storage";
import { CookieService }          from "ngx-cookie-service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  myProfile: MyProfile;
  departments: Department[];
  rolesLoading = false;
  profileLoaded = false;

  constructor(private _localSt: LocalStorageService,
              private _cookieService: CookieService,
              private _myprofileService: MyprofileService,
              private _deptService: DepartmentService,
              private _deptRoleService: DepartmentRoleService) {}

  ngOnInit() {
    this.reloadProfile();
  }

  private reloadProfile(){
    this.myProfile = this._myprofileService.getLoggedInData();
    let ssoSessionId:string = this._cookieService.get(Constants.SSOSESSIONID)

    if(!this.myProfile
      || (ssoSessionId != this.myProfile.lastLoginId)
      || (Constants.APP_VERSION != this.myProfile.version)
    ){

      this.clearLocalStorageCache();

      this._myprofileService.getProfile()
        .subscribe(myprofile => {
            this.myProfile = new MyProfile;
            this.myProfile.profile = myprofile.entity;
            this.myProfile.lastLoginId = ssoSessionId;
            this.myProfile.version = Constants.APP_VERSION;

            this.departments =  this.myProfile.profile.departments;

            this._myprofileService.setLoggedInData(this.myProfile);

            this.setDepartment();
            this.finished();
          },
          (error: AppError) => {
            console.log(error);
          });
    } else {
      this.clearLocalStorageSearchCriteria();

      this.departments =  this.myProfile.profile.departments;
      let currentDepartment = this._deptService.getCurrentDepartment();
      if (currentDepartment) {
        this.setDepartmentRoles(currentDepartment.code);
      }
      this.finished();
    }
  }

  private setDepartment() {

    let currentDepartment = this._deptService.getCurrentDepartment();
    if (!currentDepartment) {
      //set current department
      this.setFirstDepartmentAsCurrentDepartment();
    }
  };

  private setFirstDepartmentAsCurrentDepartment() {

    if (this.departments && this.departments.length > 0) {
      this._deptService.setCurrentDepartment(this.departments[0]);
      this.setDepartmentRoles(this.departments[0].code);
    }
  };

  private setDepartmentRoles(departmentCode) {

    if (departmentCode && ( !this.myProfile.profile.isSystemAdmin
        || departmentCode != this._deptRoleService.getRoleDepartment())){

      this.rolesLoading = true;

      this._myprofileService.getRolesByDepartment(departmentCode)
        .subscribe(roles => {
            this._deptRoleService.setRoles(this.myProfile.profile.id, roles.entities);
            this._deptRoleService.setRoleDepartment(departmentCode);
            this.rolesLoading = false;
            this.finished();
          },
          (error: AppError) => {
            this.rolesLoading = false;
            this.finished();
          });

    }
  };

  private finished() {
    if (!this.myProfile ||!this.departments) {
      //TODO: error handling or logout
    } else if(!this.rolesLoading){
      this.profileLoaded = true;
    }
  };

  private clearLocalStorageCache(){
    this._localSt.remove(Constants.LOGIN_DATA);
    this._localSt.remove(Constants.CURRENT_DEPARTMENT);
    this._localSt.remove(Constants.USER_ROLES);
    this.clearLocalStorageSearchCriteria();
  }

  private clearLocalStorageSearchCriteria(){
  }

}
