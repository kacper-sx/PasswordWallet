export class PaginationConfig {
  currentPage: number;
  itemsPerPage: number;
  totalItems: number;

    constructor(currentPage: number, itemsPerPage: number, totalItems: number) {
      this.currentPage = currentPage;
      this.itemsPerPage = itemsPerPage;
      this.totalItems = totalItems;
    }
}
