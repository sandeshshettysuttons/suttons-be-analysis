import { NgModule }     from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { HttpClientModule } from "@angular/common/http";
import { SharedModule } from "../../shared/shared.module";
import { FormsModule, ReactiveFormsModule} from "@angular/forms";
import { InfiniteScrollModule } from 'ngx-infinite-scroll';



import { MailRecipientService } from "../../services/mailRecipient.service";
import {MailRecipientFormComponent} from "./mailRecipient-form/mailRecipient-form.component";
import {MailRecipientListComponent} from "./mailRecipient-list/mailRecipient-list.component";
import {CompanyService} from "../../services/company.service";

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
    MailRecipientFormComponent,
    MailRecipientListComponent
  ],
  exports:[
  ],
  providers:[
    MailRecipientService,
    CompanyService
  ]
})
export class MailRecipientModule{

}
