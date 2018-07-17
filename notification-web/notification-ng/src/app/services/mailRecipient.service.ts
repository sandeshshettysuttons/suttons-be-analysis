import { Injectable } from "@angular/core";
import { ApiService } from "./api.service";
import { HttpClient } from "@angular/common/http";
import { LocalStorageService } from "angular-2-local-storage";

@Injectable()
export class MailRecipientService extends ApiService{

  resourceApiUri = "/mailRecipients";

  constructor(http: HttpClient,
              public _localSt: LocalStorageService) {
    super('/mailRecipients', http, _localSt);
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

      if (searchCriteria.company && searchCriteria.company != 'All') {

        if (searchCriteria.company == 'Global') {
          _queryParam += (firstCriteria ? '': '+')+'type('+searchCriteria.company+')';
          firstCriteria = false;

        } else {
          _queryParam += (firstCriteria ? '' : '+') + 'companyId(' + searchCriteria.company.id + ')';
          firstCriteria = false;
        }

      }

    }

    _queryParam = firstCriteria ? null : _queryParam;

    return this.get(this.resourceApiUri, 'mailRecipient(*)+company(*)', _queryParam, _orderBy, _reverse, null, true);
  }

  getById(id){
    let newUrl = this.resourceApiUri + '/' + id;
    return this.get(newUrl, 'mailRecipient(*)+company(*)', null, null, null, null, true);
  }

}
