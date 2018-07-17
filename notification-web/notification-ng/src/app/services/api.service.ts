import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { environment} from "../../environments/environment";

import { throwError as observableThrowError,  Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

import { AppError } from './../common/app-error';
import { BadInput } from './../common/bad-input';
import { ConflictError } from "../common/conflict-error";
import { Constants }    from "../common/constants";
import { NotFoundError } from './../common/not-found-error';
import { LocalStorageService } from "angular-2-local-storage";

import { Department }   from "../model/department.model";
import { ResponseData } from "../model/response.model";

@Injectable()
export class ApiService {

  baseApiUri = environment.apiUrl;
  defaultLimit = 100;
  headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private url: string, private http: HttpClient, public _localSt: LocalStorageService) {
  }

  get(newUrl?: string, expand?: string, query?: string, order?: string,
      reverse?: boolean, limit?: number, excludeDepartment?: boolean) {

    return this.http.get(this.createApiUrlWithParam(newUrl, expand, query, order, reverse, limit, excludeDepartment))
      .pipe(map(response => new ResponseData(response)),
        catchError(this.handleError));
  }

  getImage(newUrl?: string, expand?: string, query?: string, order?: string,
           reverse?: boolean, limit?: number, excludeDepartment?: boolean) {
    return this.http.get(this.createApiUrlWithParam(newUrl, expand, query, order, reverse, limit, excludeDepartment), {responseType: 'blob'})
      .pipe(map(response => {
          let urlCreator = window.URL;
          return urlCreator.createObjectURL(response);
        }),
        catchError(this.handleError));
  }

  getWithMultiOrders(newUrl?: string, expand?: string, query?: string, orders?: string[],
      reverses?: boolean[], limit?: number, excludeDepartment?: boolean) {

    return this.http.get(this.createApiUrlWithMultiOrderedParam(newUrl, expand, query, orders, reverses, limit, excludeDepartment))
      .pipe(map(response => new ResponseData(response)),
        catchError(this.handleError));
  }

  /* eg: Used to get nextPage data*/
  getByURL(newUrl: string) {
    return this.http.get(this.formattedApiUrl(newUrl))
      .pipe(map(response => new ResponseData(response)),
        catchError(this.handleError));
  }

  create(resource, excludeDepartment?: boolean) {
    return this.http.post(this.createApiUrl(excludeDepartment), JSON.stringify(resource), {headers: this.headers})
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  createByUrl(url, resource, excludeDepartment?: boolean) {
    return this.http.post(this.createApiUrl(excludeDepartment,url), JSON.stringify(resource), {headers: this.headers})
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  update(resource, excludeDepartment?: boolean) {
    return this.http.put(this.createApiUrl(excludeDepartment) + '/' + resource.id, JSON.stringify(resource), {headers: this.headers})
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  updateByUrl(url, resource, excludeDepartment?: boolean) {
    return this.http.put(this.createApiUrl(excludeDepartment,url), JSON.stringify(resource), {headers: this.headers})
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  delete(id, excludeDepartment?: boolean) {
    return this.http.delete(this.createApiUrl(excludeDepartment) + '/' + id)
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  private createApiUrl(excludeDepartment?: boolean, newUrl?: string){
    return this.baseApiUri +
      ((excludeDepartment) ? '' : ('/'+this.getCurrentDepartmentCode())) +
      ((newUrl) ? newUrl : this.url);
  }

  private getCurrentDepartmentCode(){
    return (<Department> this._localSt.get(Constants.CURRENT_DEPARTMENT)).code;
  }

  public createApiUrlWithParam(newUrl: string, expand: string, query: string, order: string,
                          reverse: boolean, limit: number, excludeDepartment: boolean){

    let param = '?' +
      (expand ? 'expand=' + expand + '&' : '') +
      (query ? 'q=' + this.formattedParams(query) + '&' : '') +
      (order ? 'order=(' + order + ' ' + (reverse ? "DESC" : "ASC") + ')&' : '') +
      ('limit=' + ((limit) ? limit : this.defaultLimit));

    return this.formattedApiUrl(this.createApiUrl(excludeDepartment, newUrl) + param);
  }

  public createApiUrlWithMultiOrderedParam(newUrl: string, expand: string, query: string, orders: string[],
                               reverses: boolean[], limit: number, excludeDepartment: boolean){

    let orderParams = '';
    if (orders) {
      orderParams = 'order=(';

      if (orders.length == reverses.length) {
        let i=0;
        for (let order of orders) {
          if (i>0) {
            orderParams += ',';
          }
          orderParams += (order ? order + ' ' + (reverses[i] ? "DESC" : "ASC") : '')
          i++;
        }
      }
      orderParams += ')&';
    }

    let param = '?' +
      (expand ? 'expand=' + expand + '&' : '') +
      (query ? 'q=' + this.formattedParams(query) + '&' : '') +
      orderParams +
      ('limit=' + ((limit) ? limit : this.defaultLimit));

    return this.formattedApiUrl(this.createApiUrl(excludeDepartment, newUrl) + param);
  }

  public uploadFiles(resource, file , params, excludeDepartment?: boolean) {
    let formData:FormData = new FormData();
    let url = this.createApiUrl(excludeDepartment, resource);
    let headers = new HttpHeaders();
    headers.append('Accept', 'application/json');

    formData.append('file', file, file.name);

    return this.http.post(url, formData, { headers: headers, params: params })
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  public updateImported(resourceURL, excludeDepartment, vehIds) {
   let url = this.createApiUrl(excludeDepartment,resourceURL);
   let textHeader =  new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.put(url, vehIds, {headers: textHeader})
      .pipe(map(response => response),
        catchError(this.handleError));
  }

  disable(id, excludeDepartment?: boolean) {
    return this.http.delete(this.createApiUrl(excludeDepartment) + '/' + id)
      .pipe(map(response => response))
      .subscribe((item: Object) => {
        console.log('Inside post')
      })
  }


  private formattedApiUrl(apiUrl: string){
    return apiUrl.replace(/\+/g, "%2B");
  }

  private formattedParams(query: string) {
    return query.replace('&', '%26');
  }

  private handleError(error: HttpErrorResponse) {
    let errorObj = (error.error && !(typeof error.error === "string")) ? error.error : error;

    if (error.status === 400)
      return observableThrowError(new BadInput(errorObj));

    if (error.status === 404)
      return observableThrowError(new NotFoundError(errorObj));

    if (error.status === 409)
      return observableThrowError(new ConflictError(errorObj));

    if (error.message.startsWith("Http failure during parsing")) {
      window.location.href = Constants.LOGOUT_URI
    }

    return observableThrowError(new AppError(errorObj));
  }
}
