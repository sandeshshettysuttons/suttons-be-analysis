/**
 * Created by nirmala.batuwitage on 02/08/2017.
 */
import {BaseModel} from "./base.model";
import {Department} from "./department.model";
import {Template} from "./template.model";

export class UserAccess extends BaseModel{

  department: Department;
  template: Template;
  departmentId: number;
  isStaff: boolean;

}
