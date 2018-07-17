import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-footer',
  template: `
    <div class="footer">
        Copyright &copy; {{year}} Suttons Motors Group. <span class="line-btn-link" style="float:none;" (click)="openReleaseNotes()" title="Version:  "> (Version: 1.0.0.0) </span>
    </div>
    `,
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {

  year  : number;

  ngOnInit() {
    this.year = new Date().getFullYear();
  }

  openReleaseNotes() {
    window.open("/${context.path}/releaseNotes.html", "Release notes", 'height=600,width=800,resizable=yes,scrollbars=yes,menubar=no,location=no,status=no,toolbar=no,directories=no');
  }

}
