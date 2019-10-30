import jdk.nashorn.internal.ir.Assignment;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class IndexManager {

    private IndexWriter indexWriter;
    @Before
    public void init() throws Exception{
        indexWriter = new IndexWriter(FSDirectory.open(new File("D:\\Study\\Project\\index").toPath()),
                new IndexWriterConfig());
    }


    @Test
    public void indexManager() throws Exception{
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File("D:\\Study\\Project\\index").toPath()),
                new IndexWriterConfig());
        Document document = new Document();
        document.add(new TextField("name", "Assignment", Field.Store.YES));
        document.add(new StoredField("path","D:\\Study\\Project"));
        document.add(new TextField("content","Assignment is hard!",Field.Store.NO));
       indexWriter.addDocument(document);
       indexWriter.close();
    }

    @Test
    public void deleteAll() throws Exception{
        indexWriter.deleteAll();
        indexWriter.close();
    }

    @Test
    public void deleteByQuery() throws Exception{
        indexWriter.deleteDocuments(new Term("name","new"));
        indexWriter.close();
    }

    @Test
    public void updateByQuery() throws Exception{
        Document document = new Document();
        document.add(new TextField("name","Hi There",Field.Store.YES));
        indexWriter.updateDocument(new Term("name","new"),document);
        indexWriter.close();
    }
}
