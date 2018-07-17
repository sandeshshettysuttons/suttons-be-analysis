import { BaseModel }       from "./base.model";

export class Lookup extends BaseModel{

  type: string;
  sequence: number;
  label: string;
  value: string;
  status: string;
}
