/**
 * Created by nirmala.batuwitage on 07/06/2018.
 */
import { RouterModule } from '@angular/router';

import { HomeComponent }  from './components/home/home.component';
import { NotFoundComponent } from './components/not-found.component'
import { AccessDeniedComponent } from './components/access-denied.component'

export const routing = RouterModule.forRoot([
  { path: '', component: HomeComponent},
  { path: 'not-found', component: NotFoundComponent},
  { path: 'access-denied', component: AccessDeniedComponent},
  { path: '**', redirectTo: 'not-found'}
]);
