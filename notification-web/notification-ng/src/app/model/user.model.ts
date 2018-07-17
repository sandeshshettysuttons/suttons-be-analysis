import {Department} from "./department.model";
import {BaseModel} from "./base.model";
import {UserAccess} from "./useraccess.model";
import {Template} from "./template.model";

export class User extends BaseModel{

  static updateColumns = 'firstName,lastName,email,phoneNumber,status,userName,loginId,password,userAccesses,isSystemAdmin';

  name: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  status: string;
  userName: string;
  loginId: string;
  password: string;
  nonADLoginSessionId: string;
  isSystemAdmin: boolean;
  departments: Department[];
  templates:Template[];

  userAccesses: UserAccess[];
  isStaffForCurrentDepartment: boolean;

}
