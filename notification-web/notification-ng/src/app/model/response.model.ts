import {PaginationData} from "./pagination.model";

export class ResponseData {

  href: string;
  pagination: PaginationData
  entity: any;
  entities: any[];

  constructor(response: any){
    if(response){
      if(response.items){
        this.href = response.href;
        this.entities = response.items;
        this.pagination = new PaginationData(
            response.count,
            response.page,
            response.limit,
            response.first,
            response.previous,
            response.next,
            response.last
        );
      }else {
        this.entity = response;
      }
    }
  }
}
