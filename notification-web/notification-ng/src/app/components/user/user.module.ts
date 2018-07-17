/**
 * Created by teddy.wong on 18/07/2017.
 */

import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RouterModule } from "@angular/router";
import { HttpClientModule } from "@angular/common/http";
import { SharedModule } from "../../shared/shared.module";
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { UserFormComponent } from "./user-form/user-form.component";
import { UserListComponent } from "./user-list/user-list.component";

import { UserService } from "../../services/user.service";
import {TemplateService} from "../../services/template.service";

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
    UserFormComponent,
    UserListComponent
  ],
  exports:[
  ],
  providers:[
    UserService,
    TemplateService
  ]
})
export class UserModule{

}
