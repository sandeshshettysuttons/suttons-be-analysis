import { RouterModule }       from "@angular/router";
import { DepartmentListComponent } from "./department-list/department-list.component";
import { DepartmentFormComponent } from "./department-form/department-form.component";

export const departmentRouting = RouterModule.forChild([
  {
    path: 'department/new',
    component: DepartmentFormComponent
  },
  {
    path: 'department/:id',
    component: DepartmentFormComponent
  },
  {
    path: 'department/:id/edit',
    component: DepartmentFormComponent
  },
  { path: 'department', component: DepartmentListComponent }
]);
