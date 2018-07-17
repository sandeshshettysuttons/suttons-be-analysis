export class PaginationData {

  constructor(
      public count: number,
      public page: number,
      public limit: number,
      public first: string,
      public previous: string,
      public next: string,
      public last: string) {
  }
}
