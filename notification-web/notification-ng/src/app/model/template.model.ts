/**
 * Created by nirmala.batuwitage on 02/08/2017.
 */
import {BaseModel} from "./base.model";
import {Role} from "./role.model";

export class Template extends BaseModel{

  name: string;
  description: string;
  roles: Role[];

  accessLevel: number;
  lowerAccessLevelFrom: number;
  lowerAccessLevelTo: number;
}
