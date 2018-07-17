import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

@Injectable()
export class LookupService extends ApiService{

  resourceApiUri = "/lookups";

  constructor(http: HttpClient, public _localSt: LocalStorageService) {
    super('/lookups', http, _localSt);
  }

  queryByType(type){

    let _queryParam = null;
    if (type) {
      _queryParam = '?'+'type('+type+')';
    }

    return this.get(this.resourceApiUri, 'lookup(*)', _queryParam);
  }
}
