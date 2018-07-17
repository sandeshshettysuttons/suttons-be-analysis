import { Component } from '@angular/core';

@Component({
    template: `
      <div id="main">
        <div style="margin: 30px; ">
          <h1 style="font-size: 28px;">
            Access Denied
          </h1>
          <p style="margin-top: 20px">
            You don't have permission to access this area.
          </p>
        </div>
      </div>
    `
})
export class AccessDeniedComponent { }
