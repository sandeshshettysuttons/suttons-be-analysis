export class MultiselectDropdown {

  id: number;
  itemName: string;
  itemValue: string;
  category: string;

  constructor(id: number, itemName: string, itemValue?: string, category?: string){
      this.id = id;
      this.itemName = itemName;
      this.itemValue = itemValue ? itemValue : '';
      this.category = category ? category : '';
  }
}
