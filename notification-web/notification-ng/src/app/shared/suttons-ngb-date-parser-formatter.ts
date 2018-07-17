import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { isNumber, toInteger } from '@ng-bootstrap/ng-bootstrap/util/util';

export class SuttonsNgbDateParserFormatter extends NgbDateParserFormatter {

  parse(value: string): NgbDateStruct {

    if (value) {
      const dateParts = value.trim().split('/');
      if (dateParts.length === 3 && isNumber(dateParts[0]) && isNumber(dateParts[1]) && isNumber(dateParts[2])) {
        return {day: toInteger(dateParts[0]), month: toInteger(dateParts[1]), year: toInteger(dateParts[2])};
      }
    }
    return null;
  }

  format(date: NgbDateStruct): string {

    if (date) {
      return `${date.day}/${date.month}/${date.year}`;
    }

    return null;
  }
}
