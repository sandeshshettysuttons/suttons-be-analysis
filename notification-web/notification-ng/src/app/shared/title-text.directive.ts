/**
 * Created by nirmala.batuwitage on 07/06/2018.
 */
import { Directive, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';

@Directive({
  selector: '[titleAndText]'
})
export class TitleTextDirective implements OnInit{
  @Input('titleAndText') titleAndText: string;

  constructor(private _el: ElementRef, private _renderer: Renderer2) { }

  ngOnInit() {
    this._el.nativeElement.textContent = this.titleAndText;
    this._renderer.setAttribute(this._el.nativeElement, 'title', this.titleAndText);
  }

}
