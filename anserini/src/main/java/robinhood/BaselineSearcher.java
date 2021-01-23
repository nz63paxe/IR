package robinhood;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import java.util.HashMap;
import java.util.Map;

public class BaselineSearcher extends AbstractSearcher implements ISearcher {

    private String name = "Baseline";
    private double versionNumber = 1.0;
    private IndexSearcher indexSearcher;

    public static void main(String[] args) throws Exception { // mvn exec:java -Dexec.mainClass="robinhood.BaselineSearcher" -Dexec.args="Should guns be banned?"
        BaselineSearcher searcher = new BaselineSearcher();
        String query = args[0];
        searcher.printResults(searcher.search(query.toLowerCase(), 10), searcher.getIndexSearcher());
    }

    public BaselineSearcher() throws Exception {
        indexSearcher = createSearcher(new LMDirichletSimilarity(), Variables.concatenatedArgumentIndexPath);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getVersionNumber() {
        return versionNumber;
    }

    @Override
    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    @Override
    public TopDocs search(String query, int maxAmount) throws Exception {
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser queryParser = new QueryParser("fullArgument", analyzer);
        Query searchQuery = queryParser.parse(query);
        TopDocs hits = indexSearcher.search(searchQuery, maxAmount);
        return hits;
    }

    /* alter Version mit getrennter Konklusion und Premisse
    @Override
    public TopDocs search(String query, int maxAmount) throws Exception {
        Analyzer analyzer = new SimpleAnalyzer();
        Map<String,Float> boosts = new HashMap<>();
        boosts.put("conclusion", 3.0f);
        boosts.put("premise", 1.0f);
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                new String[] {"conclusion", "premise"},
                analyzer,
                boosts
        );
        Query searchQuery = queryParser.parse(query);
        TopDocs hits = indexSearcher.search(searchQuery, maxAmount);
        return hits;
    }
     */

}
