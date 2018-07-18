import { BaseModel } from "./base.model";

export class Company extends BaseModel{

  static updateColumns = 'code,name,status';

  code: string;
  name: string;
  status: string;
}
