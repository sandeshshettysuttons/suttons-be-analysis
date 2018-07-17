import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";
import {Router} from "@angular/router";

@Injectable()
export class AuditLogService extends ApiService{

  resourceApiUri = "/auditLogs";

  constructor(http: HttpClient, public _localSt: LocalStorageService) {
    super('/auditLogs', http, _localSt);
  }

  queryBySearchCriteria(searchCriteria, _orderBy: string, _reverse: boolean){

    let _queryParam = '';
    let firstCriteria = true;

    if (searchCriteria) {

      if (searchCriteria.resource) {
        _queryParam += (firstCriteria ? '': '+')+'activityResource('+searchCriteria.resource+')';
        firstCriteria = false;
      }
      if(searchCriteria.resourceId) {
        _queryParam += (firstCriteria ? '': '+')+'activityResourceId('+searchCriteria.resourceId+')';
        firstCriteria = false;
      }
      if (searchCriteria.activityTypes) {
        _queryParam += (firstCriteria ? '': '+')+'activityTypes('+searchCriteria.activityTypes+')';
        firstCriteria = false;
      }
    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'auditLog(activityTimestamp,description)', _queryParam, _orderBy, _reverse);
  }

}
