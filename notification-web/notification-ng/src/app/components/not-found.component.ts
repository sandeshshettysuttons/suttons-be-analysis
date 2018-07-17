import { Component } from '@angular/core';

@Component({
    template: `
      <div id="main">
        <div style="margin: 30px; ">
          <h1 style="font-size: 28px;">
            Page Not Found
          </h1>
          <p style="margin-top: 20px">
            We're sorry, but the page you requested cannot be found.
          </p>
        </div>
      </div>
    `
})
export class NotFoundComponent { }
