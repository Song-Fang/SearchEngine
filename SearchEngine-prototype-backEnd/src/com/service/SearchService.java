package com.service;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class SearchService {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private Directory directory;
    private IndexWriter indexWriter;

    //Initialization key variables
    public SearchService() {
        try {
            directory = FSDirectory.open(new File("SearchEngine-prototype-backEnd//index").toPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Create tokens and index
    public void createIndex() throws Exception {
        if (indexWriter == null) {
            indexWriter = new IndexWriter(directory, new IndexWriterConfig());
            File dir = new File("SearchEngine-prototype-backEnd//dataSource");
            File[] files = dir.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                String filePath = file.getPath();
                String fileContent = FileUtils.readFileToString(file, "utf-8");
                long fileSize = FileUtils.sizeOf(file);
                Field fieldName = new TextField("name", fileName, Field.Store.YES);
                Field fieldPath = new StoredField("path", filePath);
                Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
                Field fieldSizeValue = new LongPoint("size", fileSize);
                Field fieldSizeStore = new StoredField("size", fileSize);
                Document document = new Document();
                document.add(fieldName);
                document.add(fieldPath);
                document.add(fieldContent);
                document.add(fieldSizeValue);
                document.add(fieldSizeStore);
                indexWriter.addDocument(document);
            }
            indexWriter.close();
        }
    }


    //Execute Query by using queryParser

    public void queryParser(String queryName, String queryContent) throws Exception {
        QueryParser queryParser = new QueryParser(queryName, new StandardAnalyzer());
        Query query = queryParser.parse(QueryParser.escape(queryContent));
        printQueryResult(query);
    }


    //print search results
    //only display 10 top results by decreasing order of relevance
    private void printQueryResult(Query query) throws Exception {
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);

        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("Total Number of Results We Found:   " + topDocs.totalHits);
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println();
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println("Document Name: "+document.get("name"));
            System.out.println("----------------------------------------------------------");
            System.out.println("Document Content fragment:");
            String content = document.get("content");
            String subContent = content.substring(0,700);

            System.out.println(subContent+"......");
            System.out.println("***********************************************************");
            System.out.println();
        }
    }


}
