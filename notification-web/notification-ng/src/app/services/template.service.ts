import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

@Injectable()
export class TemplateService extends ApiService{

  resourceApiUri = "/templates/available";

  constructor(http: HttpClient, public _localSt: LocalStorageService) {
    super('/templates/available', http, _localSt);
  }

  queryBySearchCriteria(searchCriteria, _orderBy: string, _reverse: boolean){

    let _queryParam = '';
    let firstCriteria = true;

    if (searchCriteria) {

      if (searchCriteria.userName) {
        _queryParam += (firstCriteria ? '': '+')+'userName('+searchCriteria.userName+')';
        firstCriteria = false;
      }
      if (searchCriteria.userId) {
        _queryParam += (firstCriteria ? '': '+')+'userId('+searchCriteria.userId+')';
        firstCriteria = false;
      }
    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'template(*)', _queryParam, _orderBy, _reverse);
  }

  getById(id){
    let newUrl = this.resourceApiUri + '/' + id;
    return this.get(newUrl, 'template(*)');
  }

}
