import { RouterModule } from "@angular/router";
import { UserListComponent } from "./user-list/user-list.component";
import { UserFormComponent } from "./user-form/user-form.component";

export const userRouting = RouterModule.forChild([
  {
    path: 'users/new',
    component: UserFormComponent
  },
  {
    path: 'users/:id',
    component: UserFormComponent
  },
  {
    path: 'users/:id/edit',
    component: UserFormComponent
  },
  { path: 'users', component: UserListComponent }
]);
