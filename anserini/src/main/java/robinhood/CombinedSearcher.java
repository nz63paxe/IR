package robinhood;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.elasticsearch.search.aggregations.metrics.TopHits;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CombinedSearcher extends AbstractSearcher implements ISearcher  {

    private String name = "CombinedSearcher";
    private double versionNumber = 1.0;
    private IndexSearcher indexSearcherDirichlet;
    private IndexSearcher indexSearcherBM25;

    public static void main(String[] args) throws Exception { // mvn exec:java -Dexec.mainClass="robinhood.CombinedSearcher" -Dexec.args="Should guns be banned?"
        CombinedSearcher searcher = new CombinedSearcher();
        String query = args[0];
        searcher.printResults(searcher.search(query, 20), searcher.getIndexSearcher());
    }

    public CombinedSearcher() throws Exception {
        indexSearcherBM25 = createSearcher(new BM25Similarity(), Variables.concatenatedArgumentIndexPath);
        indexSearcherDirichlet = createSearcher(new LMDirichletSimilarity(), Variables.concatenatedArgumentIndexPath);
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
        return indexSearcherBM25;
    }

    @Override
    public TopDocs search(String query, int maxAmount) throws Exception {
        TopDocs bm25Results = baselineSearch(indexSearcherBM25, query.toLowerCase(), maxAmount / 2);
        TopDocs dirichletResults = baselineSearch(indexSearcherDirichlet, query.toLowerCase(), maxAmount / 2);
        ScoreDoc[] combined = new ScoreDoc[maxAmount];
        combined = ArrayUtils.addAll(bm25Results.scoreDocs, dirichletResults.scoreDocs);
        Arrays.sort(combined, new Comparator<ScoreDoc>() {
            @Override
            public int compare(ScoreDoc s1, ScoreDoc s2) {
                if(s1.score < s2.score)
                    return 1;
                else if (s1.score == s2.score)
                    return 0 ;
                return -1 ;
            }
        });
        TotalHits totalHits = new TotalHits(bm25Results.totalHits.value + dirichletResults.totalHits.value, bm25Results.totalHits.relation);
        return new TopDocs(totalHits, combined);
    }

    public TopDocs baselineSearch(IndexSearcher indexSearcher, String query, int maxAmount) throws Exception {
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser queryParser = new QueryParser("fullArgument", analyzer);
        Query searchQuery = queryParser.parse(query);
        TopDocs hits = indexSearcher.search(searchQuery, maxAmount);
        return hits;
    }

}
