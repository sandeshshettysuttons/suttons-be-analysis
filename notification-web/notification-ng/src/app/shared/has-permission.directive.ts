/**
 * Created by nirmala.batuwitage on 07/06/2018.
 */
import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { PermissionService } from "../services/permission.service";

@Directive({
  selector: '[hasPermission]'
})
export class HasPermissionDirective implements OnInit{
  @Input('hasPermission') permission: string;

  constructor(private _el: ElementRef, private _permission: PermissionService) { }

  ngOnInit() {
    if(!this._permission.hasPermission(this.permission)){
      this._el.nativeElement.style.display = 'none';
    }
  }


}
