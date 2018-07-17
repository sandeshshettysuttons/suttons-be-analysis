import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

@Injectable()
export class CompanyService extends ApiService{

  resourceApiUri = "/companies";

  constructor(http: HttpClient,
              public _localSt: LocalStorageService) {
    super('/companies', http, _localSt);
  }

  queryBySearchCriteria(searchCriteria, _orderBy: string, _reverse: boolean){

    let _queryParam = '';
    let firstCriteria = true;

    if (searchCriteria) {

      if (searchCriteria.name) {
        _queryParam += (firstCriteria ? '': '+')+'name('+searchCriteria.name+')';
        firstCriteria = false;
      }
      if (searchCriteria.status) {
        _queryParam += (firstCriteria ? '': '+')+'status('+searchCriteria.status+')';
        firstCriteria = false;
      }
    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'company(name,status)', _queryParam, _orderBy, _reverse, null, true);
  }

  getById(id){
    let newUrl = this.resourceApiUri + '/' + id;
    return this.get(newUrl, 'company(version,name,status)', null, null, null, null, true);
  }

}
