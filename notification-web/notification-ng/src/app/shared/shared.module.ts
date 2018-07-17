import { NgModule }             from '@angular/core';
import { CommonModule }         from '@angular/common';

import { SpinnerComponent }     from './spinner.component';
import { PermissionService }    from '../services/permission.service';

import { TitleTextDirective }   from "./title-text.directive";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    SpinnerComponent,
    TitleTextDirective
  ],
  exports: [
    SpinnerComponent,
    TitleTextDirective
  ],
  providers: [
    PermissionService
  ]
})
export class SharedModule {
}
