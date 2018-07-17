import { RouterModule }       from "@angular/router";
import { CompanyListComponent } from "./company-list/company-list.component";
import { CompanyFormComponent } from "./company-form/company-form.component";

export const companyRouting = RouterModule.forChild([
  {
    path: 'company/new',
    component: CompanyFormComponent
  },
  {
    path: 'company/:id',
    component: CompanyFormComponent
  },
  {
    path: 'company/:id/edit',
    component: CompanyFormComponent
  },
  { path: 'company', component: CompanyListComponent }
]);
