/**
 * Created by nirmala.batuwitage on 07/06/2018.
 */

export class AppError{

  apiMessageHeader = 'The requested operation failed for the following reason(s):';
  constructor(public originalError?: any){
  }

  get message() {
    let msg = '';
    if (this.originalError) {
      msg = (this.originalError.message) ? this.originalError.message : this.originalError.statusText;

      var index = msg.indexOf(this.apiMessageHeader);
      if (index >= 0) {
        msg = msg.substring(index + this.apiMessageHeader.length)
      }
    }
    return msg.trim();
  }

}
