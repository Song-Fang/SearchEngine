package com.view;

import com.service.SearchService;

public class Client {

    public static void main(String [] args) throws Exception{
        SearchService searchService = new SearchService();
        searchService.createIndex();
        String queryName = "name";
        String queryContent = "document";
        searchService.queryParser(queryName,queryContent);

    }
}
