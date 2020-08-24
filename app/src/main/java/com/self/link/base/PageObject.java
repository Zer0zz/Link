package com.self.link.base;

public class PageObject {
    /**
     * {
     *     "total": 3,
     *     "list": [
     *       {
     *
     *       },
     *       {
     *
     *       },
     *       {
     *
     *       }
     *     ],
     *     "pageNum": 1,
     *     "pageSize": 5,
     *     "size": 3,
     *     "startRow": 1,
     *     "endRow": 3,
     *     "pages": 1,
     *     "prePage": 0,
     *     "nextPage": 0,
     *     "isFirstPage": true,
     *     "isLastPage": true,
     *     "hasPreviousPage": false,
     *     "hasNextPage": false,
     *     "navigatePages": 8,
     *     "navigatepageNums": [
     *       1
     *     ],
     *     "navigateFirstPage": 1,
     *     "navigateLastPage": 1,
     *     "firstPage": 1,
     *     "lastPage": 1
     *   }
     */

    public int total;
    public String list;
    public int pageNum;
    public int pageSize;
    public int startRow;
    public int endRow;
    public int pages;
    public int prePage;
    public int nextPage;
    public boolean isFirstPage ;
    public boolean isLastPage;
    public boolean hasPreviousPage ;
    public boolean hasNextPage;
    public int navigatePages;
    public String navigatepageNums;

    public int navigateFirstPage;
    public int navigateLastPage;
    public int firstPage;
    public int lastPage;

}
