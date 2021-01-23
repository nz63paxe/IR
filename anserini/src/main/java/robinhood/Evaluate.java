package robinhood;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
//import org.w3c.dom.Document; not required because called it specifically
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;


public class Evaluate {
    public static void main(String[] args) throws Exception { // mvn exec:java -Dexec.mainClass="robinhood.Evaluate"
        evaluate(new BaselineSearcher(), 10);
        evaluate(new PseudoRelevanceSearcher(), 10);
    }
    
    
    public static void evaluate(ISearcher isearcher, int maxAmount) throws Exception {
    	IndexSearcher searcher = isearcher.getIndexSearcher();
    	String appname = isearcher.getName() + String.valueOf(isearcher.getVersionNumber());
    	System.out.println("Evaluate "+appname);
    	// create result file
    	try {
    		File myObj = new File(Variables.evalOutputPath+appname);
    		if (myObj.createNewFile()) {
    			System.out.println("Output file created!");
    		} else {
    			System.out.println("File already exists!");
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	List<Topic> topics = importXML();
    	for (Topic topic : topics) {
    		TopDocs foundDocs = isearcher.search(topic.getTitle(), maxAmount);
    		System.out.println("Total Results for "+topic.getTitle()+": " + foundDocs.totalHits);
    		int rank = 1;
    		for (ScoreDoc sd : foundDocs.scoreDocs) {
	              Document d = searcher.doc(sd.doc);
//	              System.out.println("Conclusion: " + d.get("conclusion"));
//	              System.out.println("Premise: " + d.get("premise"));
//	              System.out.println("Topic:"+topic.getTitle() +" Rank:"+ String.valueOf(rank) +" ID:"+ d.get("id"));
	              String topic_number = topic.getNumber();
	              String literal = "Q0";
	              String doc_id = d.get("id");
	              String rank_str = String.valueOf(rank);
	              String score = String.valueOf(sd.score);
	              try {
	            	  FileWriter myWriter = new FileWriter(Variables.evalOutputPath+appname, true); // should be in here
	            	  myWriter.write(topic_number+"\t"+literal+"\t"+doc_id+"\t"+rank_str+"\t"+score+"\t"+appname+"\n");
	                  myWriter.close();
	              } catch(IOException e) {
	            	  e.printStackTrace();
	              }
	              rank = rank + 1;
	          }
    		
    	}
    }
    
    private static List<Topic> importXML() {
    	List<Topic> topics = new ArrayList<Topic>();
    	try {

    	    File fXmlFile = new File(Variables.evalTopicsPath);
    	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	    org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
    	            
    	    //optional, but recommended
    	    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    	    doc.getDocumentElement().normalize();

//    	    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    	            
    	    NodeList nList = doc.getElementsByTagName("topic");
    	            
//    	    System.out.println("import topics");

    	    for (int temp = 0; temp < nList.getLength(); temp++) {
    	    	String number;
    	    	String title;

    	        Node nNode = nList.item(temp);
    	                
//    	        System.out.println("\nCurrent Element :" + nNode.getNodeName());
    	                
    	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    	            Element eElement = (Element) nNode;
    	            
    	            number = eElement.getElementsByTagName("number").item(0).getTextContent();
    	            title = eElement.getElementsByTagName("title").item(0).getTextContent();
    	            
    	        topics.add(new Topic(number, title));
    	        }
    	    }
    	    } catch (Exception e) {
    	    e.printStackTrace();
    	    }
    	return topics;
    }

}