import { BaseModel } from "./base.model";
import { Company } from "./company.model";

export class MailRecipient extends BaseModel{

  static updateColumns = 'name,email,type,status,company';

  name: string;
  email: string;
  type: string;
  status: string;

  company: Company;
}
