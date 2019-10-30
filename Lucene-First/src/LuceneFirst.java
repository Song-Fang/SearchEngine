import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;

public class LuceneFirst {
    @Test
    public void createIndex() throws Exception{
        Directory directory = FSDirectory.open(new File("D:\\Study\\Project\\index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig());
        File dir = new File("D:\\Study\\Project\\searchSource");
        File [] files = dir.listFiles();
        for(File file:files){
            String fileName = file.getName();
            String filePath = file.getPath();
            String fileContent = FileUtils.readFileToString(file,"utf-8");
            long fileSize = FileUtils.sizeOf(file);
            Field fieldName = new TextField("name",fileName, Field.Store.YES);
            Field fieldPath = new StoredField("path",filePath);
            Field fieldContent = new TextField("content",fileContent,Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size",fileSize);
            Field fieldSizeStore = new StoredField("size",fileSize);
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

    @Test
    public void rangeSearch() throws Exception{
        Directory directory = FSDirectory.open(new File("D:\\Study\\Project\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("name","new"));
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("查询总记录数"+topDocs.totalHits);
        ScoreDoc [] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println("-------------------");
        }

        indexReader.close();

    }

    @Test
    public void tokenStream() throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("","SpringBoot is a effective java WEB framework");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()){
            System.out.println(charTermAttribute);
        }

        tokenStream.close();

    }

    @Test
    public void rangeQuery() throws Exception{
        Directory directory = FSDirectory.open(new File("D:\\Study\\Project\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = LongPoint.newRangeQuery("size",1,1000);
        TopDocs topDocs = indexSearcher.search(query,5);
        System.out.println("查询总记录数"+topDocs.totalHits);
        ScoreDoc [] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("size"));
            System.out.println(document.get("content"));
            System.out.println("-------------------");
        }

        indexReader.close();
    }
}

