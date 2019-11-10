package com.view;

import com.service.SearchService;

import java.util.Scanner;

public class Client {
    private static SearchService searchService =new SearchService();;
    private static String queryName = null;
    private static String queryContent = null;
    private static Scanner scan = new Scanner(System.in);

    public static void main(String [] args) throws Exception{
        System.out.println();
        System.out.println("-----------Search Engine beta1.0-----------");
        System.out.println("Not necessary to create index if index already exists!");
        System.out.println("--------------------------------------------------------");
            System.out.println("Do you need to create index: Y/N");

            String indexConfirm = scan.next();

            while(true) {
                if (indexConfirm.equals("Y") || indexConfirm.equals("y")) {
                    searchService.createIndex();
                    search();
                    System.out.println("System Terminate, See You!");
                    break;

                } else if (indexConfirm.equals("N") || indexConfirm.equals("n")) {
                    search();
                    System.out.println("System Terminate, See You!");
                    break;
                } else if(indexConfirm.toLowerCase().equals("exit")){
                    System.out.println("System Terminate, See You!");
                    break;
                }else{
                    System.out.println("Invalid input, please input again or exit typing \'exit\'");
                    indexConfirm = scan.next();
                    continue;
                }
            }

    }

    private static void search() throws Exception{
        System.out.println("Search By FileName/Search By Content");
        while(true) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Press 1 if you search by FileName or 2 by Content");
            System.out.println("Press 3 to exit");
            System.out.println("-----------------------------------------------------");
            String queryType = scan.next();
            if (queryType.equals("1")) {
                queryName = "name";
                System.out.println("Input your key words:");
                queryContent = scan.next();
                searchService.queryParser(queryName, queryContent);
            } else if (queryType.equals("2")) {
                queryName = "content";
                System.out.println("Input your key words:");
                queryContent = scan.next();
                searchService.queryParser(queryName, queryContent);
            } else if (queryType.equals("3")) {
                return;
            } else {
                System.out.println("Invalid input, please input again");
                continue;
            }
        }
    }
}
