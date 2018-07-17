import { BaseModel } from "./base.model";

export class Company extends BaseModel{

  static updateColumns = 'name,status';

  name: string;
  status: string;
}
