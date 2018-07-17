import { Component, Input } from '@angular/core';

@Component({
  selector: 'spinner',
  template: `
      <div class="spinnerContent">
        <i *ngIf="visible" class="fa fa-spinner fa-spin fa-2x"></i>
      </div>
    `
})
export class SpinnerComponent {
  @Input() visible = true;
}
