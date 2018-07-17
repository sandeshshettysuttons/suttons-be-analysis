import { NgModule }     from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { HttpClientModule } from "@angular/common/http";
import { SharedModule } from "../../shared/shared.module";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { CompanyFormComponent } from "./company-form/company-form.component";
import { CompanyListComponent } from "./company-list/company-list.component";

import { CompanyService } from "../../services/company.service";

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
    CompanyFormComponent,
    CompanyListComponent
  ],
  exports:[
  ],
  providers:[
    CompanyService
  ]
})
export class CompanyModule{

}
