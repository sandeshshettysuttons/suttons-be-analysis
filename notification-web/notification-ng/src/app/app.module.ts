import { BrowserModule }      from '@angular/platform-browser';
import { NgModule }           from '@angular/core';
import { FormsModule }        from "@angular/forms";
import { NgbModule }          from '@ng-bootstrap/ng-bootstrap';

import { LocalStorageModule } from 'angular-2-local-storage';
import { CookieService }      from 'ngx-cookie-service';

import { DepartmentModule }   from "./components/department/department.module";
import { UserModule }         from "./components/user/user.module";

import { departmentRouting }  from "./components/department/department.routing";
import { routing }            from "./app.routing";
import { userRouting }        from "./components/user/user.routing";

import { AccessDeniedComponent } from "./components/access-denied.component";
import { AppComponent }       from './app.component';
import { HomeComponent }      from './components/home/home.component';
import { NavbarComponent }    from './navbar/navbar.component';
import { FooterComponent }    from "./footer/footer.component";
import { NotFoundComponent }  from "./components/not-found.component";

import { DepartmentService }  from "./services/department.service";
import { DepartmentRoleService } from "./services/department-role.service";
import { MyprofileService }   from "./services/myprofile.service";
import { LookupService }      from "./services/lookup.service";

import { HasPermissionDirective } from './shared/has-permission.directive';

import {mailRecipientRouting} from "./components/mailRecipient/mailRecipient.routing";
import {MailRecipientModule} from "./components/mailRecipient/mailRecipient.module";
import {CompanyModule} from "./components/company/company.module";
import {companyRouting} from "./components/company/company.routing";

@NgModule({
  declarations: [
    AccessDeniedComponent,
    AppComponent,
    HasPermissionDirective,
    HomeComponent,
    FooterComponent,
    NavbarComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    DepartmentModule,
    FormsModule,
    NgbModule.forRoot(),
    LocalStorageModule.withConfig({prefix: 'notification',storageType: 'localStorage'}),
    UserModule,
    CompanyModule,
    MailRecipientModule,

    departmentRouting,
    userRouting,
    companyRouting,
    mailRecipientRouting,
    routing,
  ],
  providers: [
    DepartmentService,
    DepartmentRoleService,
    MyprofileService,
    CookieService,
    LookupService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
