import { BaseModel } from "./base.model";

export class Department extends BaseModel{

  static updateColumns = 'code,name,isActive';

  code: string;
  name: string;
  isActive: boolean;
}
