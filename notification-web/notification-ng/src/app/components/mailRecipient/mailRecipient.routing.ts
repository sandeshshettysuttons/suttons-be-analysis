import { RouterModule }       from "@angular/router";
import { MailRecipientListComponent } from "./mailRecipient-list/mailRecipient-list.component";
import { MailRecipientFormComponent } from "./mailRecipient-form/mailRecipient-form.component";

export const mailRecipientRouting = RouterModule.forChild([
  {
    path: 'mailRecipient/new',
    component: MailRecipientFormComponent
  },
  {
    path: 'mailRecipient/:id',
    component: MailRecipientFormComponent
  },
  {
    path: 'mailRecipient/:id/edit',
    component: MailRecipientFormComponent
  },
  { path: 'mailRecipient', component: MailRecipientListComponent }
]);
