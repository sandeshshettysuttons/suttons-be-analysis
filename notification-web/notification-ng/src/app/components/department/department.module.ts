import { NgModule }     from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { HttpClientModule } from "@angular/common/http";
import { SharedModule } from "../../shared/shared.module";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { DepartmentFormComponent } from "./department-form/department-form.component";
import { DepartmentListComponent } from "./department-list/department-list.component";

import { DepartmentService } from "../../services/department.service";

@NgModule({
  imports:[
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    SharedModule,
    InfiniteScrollModule
  ],
  declarations:[
    DepartmentFormComponent,
    DepartmentListComponent
  ],
  exports:[
  ],
  providers:[
    DepartmentService
  ]
})
export class DepartmentModule{

}
