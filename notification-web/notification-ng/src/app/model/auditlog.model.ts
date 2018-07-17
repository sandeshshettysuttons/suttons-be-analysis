import {BaseModel} from "./base.model";

export class Auditlog extends BaseModel {

  static updateColumns = "";

  activityType: string;
  activityResource: string;
  activityResourceId: number;
  activityDate : Date;
  description: string;
  activityTimestamp: string;

}
