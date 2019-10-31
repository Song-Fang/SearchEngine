import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class LuceneFirst {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private Directory directory;

    @Before
    public void init() throws Exception{
        directory = FSDirectory.open(new File("D:\\Study\\Project\\index").toPath());
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
    }

    @Test
    public void createIndex() throws Exception{
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
    public void indexSearch() throws Exception{

        Query query = new TermQuery(new Term("name","new"));
        printQueryResult(query);



        indexReader.close();

    }

    private void printQueryResult(Query query) throws Exception{
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("查询总记录数"+topDocs.totalHits);
        ScoreDoc [] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            int docId = scoreDoc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println("-------------------");
        }
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
        Query query = LongPoint.newRangeQuery("size",1,1000);
        printQueryResult(query);
        indexReader.close();
    }

    @Test
    public void queryParser() throws Exception{
        QueryParser queryParser = new QueryParser("name",new StandardAnalyzer());
        Query query = queryParser.parse("A large text");
        printQueryResult(query);
    }
}

