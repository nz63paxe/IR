package robinhood;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Utf8;
import com.google.gson.stream.JsonReader;
import net.sf.extjwnl.data.Exc;
import org.apache.logging.log4j.message.Message;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import com.google.gson.*;
import org.elasticsearch.index.store.Store;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IndexArguments extends AbstractIndexer {

    public static void main(String[] args) throws Exception {
        IndexArguments indexArguments = new IndexArguments();
        indexArguments.index();
    }

    private void index() throws Exception {
        Gson gson = new Gson();
        IndexWriter writer = createWriter(Variables.argumentIndexPath);
        writer.deleteAll();
        writer.commit();
        for (String path : Variables.jsonFiles) {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            List<Document> allArguments = new ArrayList<>();
            reader.beginObject();
            reader.skipValue();
            reader.beginArray();
            while (reader.hasNext()) {
                JsonObject js = gson.fromJson(reader, JsonObject.class);
                JsonObject premise = js.get("premises").getAsJsonArray().get(0).getAsJsonObject();
                allArguments.add(createArgument(
                        js.get("id").toString(),
                        js.get("conclusion").toString(),
                        premise.get("text").toString(),
                        premise.get("stance").toString(),
                        js.getAsJsonObject().toString()
                ));
                if (allArguments.size() == 10_000) {
                    writer.addDocuments(allArguments);
                    writer.flush();
                    allArguments.clear();
                }
            }
            reader.endArray();
            reader.endObject();
            reader.close();
            writer.addDocuments(allArguments);
            writer.flush();
            writer.commit();
            allArguments.clear();
            System.out.println("Finished " + path);
        }
        writer.close();
    }

    private Document createArgument(String id, String conclusion, String premise, String premiseStance, String fullJson) {
        Document document = new Document();
        document.add(new TextField("conclusion", conclusion , Field.Store.YES));
        document.add(new TextField("premise", premise , Field.Store.YES));
        document.add(new StoredField("id", id));
        document.add(new StoredField("premiseStance", premiseStance));
        document.add(new StoredField("fullJson", fullJson));
        return document;
    }

}
