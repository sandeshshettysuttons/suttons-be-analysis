import { Component, Input, OnInit } from '@angular/core';
import { NgbPanelChangeEvent } from "@ng-bootstrap/ng-bootstrap";

import { AppError }       from "../../common/app-error";

import { Auditlog }       from "../../model/auditlog.model";
import { PaginationData } from "../../model/pagination.model";

import { AuditLogService} from "../../services/auditlog.service";

@Component({
  selector: 'audit-logs',
  templateUrl: './auditlog.component.html',
  styleUrls: ['./auditlog.component.css']
})
export class AuditlogComponent implements OnInit {
  @Input() resource: string;
  @Input() resourceId: number;
  @Input() activityTypes: string;

  auditLogs: Auditlog[];
  pagination: PaginationData;

  searchCriteria = {
    resource: undefined,
    resourceId: undefined,
    activityTypes: undefined
  };

  orderBy = "lastUpdateTS";
  reverse = true;

  searching = false;

  constructor(private auditLogService: AuditLogService) { }

  ngOnInit() {
  }

  public beforeChange($event: NgbPanelChangeEvent) {

    if ($event.nextState === true) {
      this.search();
    }
  };

  search() {
    this.searching = true;
    this.searchCriteria.resource = this.resource;
    this.searchCriteria.resourceId = this.resourceId;
    this.searchCriteria.activityTypes = this.activityTypes;

    this.auditLogService.queryBySearchCriteria(this.searchCriteria, this.orderBy, this.reverse)
      .subscribe(activities => {
        this.auditLogs = activities.entities;
        this.pagination = activities.pagination;

        this.searching = false;
      },(error: AppError) => {
        this.searching = false;
      });
  }

  displayMoreItems(){
    if(this.searching){
      return;
    }

    if(this.pagination && this.pagination.next){
      this.searching = true;
      this.auditLogService.getByURL(this.pagination.next)
        .subscribe(activities => {
            for(let auditLog of activities.entities){
              this.auditLogs.push(auditLog);
            }
            this.pagination = activities.pagination;

            this.searching = false;
          },
          (error: AppError) => {
            this.searching = false;
          });
    }
  }

}
