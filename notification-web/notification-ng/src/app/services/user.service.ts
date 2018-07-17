import {Injectable} from "@angular/core";
import {ApiService} from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

@Injectable()
export class UserService extends ApiService{
  resourceApiUri = "/users";

  constructor(http: HttpClient, public _localSt: LocalStorageService) {
    super('/users', http, _localSt);
  }

  queryBySearchCriteria(searchCriteria, _orderBy: string, _reverse: boolean){

    let _queryParam = '';
    let firstCriteria = true;

    if (searchCriteria) {

      if (searchCriteria.typeahead) {
        _queryParam += (firstCriteria ? '': '+')+'typeahead('+searchCriteria.typeahead+')';
        firstCriteria = false;
      }
      if (searchCriteria.isActive) {
        _queryParam += (firstCriteria ? '': '+')+'isActive('+searchCriteria.isActive+')';
        firstCriteria = false;
      }
    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'user(*)+userAccess(*)+department(*)+template(*)', _queryParam, _orderBy, _reverse);
  }

  getById(id){
    let newUrl = this.resourceApiUri + '/' + id;
    return this.get(newUrl, 'user(*)+userAccess(*)+department(*)+template(*)', );
  }
}
