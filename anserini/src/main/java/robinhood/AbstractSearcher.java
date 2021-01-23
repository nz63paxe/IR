package robinhood;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Einige Methoden die beim Erstellen bzw. Nutzen eines Searchers hilfreich sein können.
 */
public abstract class AbstractSearcher {

    IndexSearcher createSearcher(Similarity sim, String indexPath) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(sim);
        return searcher;
    }

    void printResults(TopDocs foundDocs, IndexSearcher searcher) {
        System.out.println("Total Results: " + foundDocs.totalHits + "\n");
        int rank = 1;
        for (ScoreDoc sd : foundDocs.scoreDocs) {
            try {
                Document d = searcher.doc(sd.doc);
                System.out.println("Rank " + rank + ":");
                rank++;
                System.out.println("Score: " + sd.score);
                System.out.println("Conclusion: " + d.get("conclusion"));
                System.out.println("Premise: " + d.get("premise"));
                System.out.println("Stance: " + d.get("premiseStance"));
                System.out.println();
            } catch (Exception e) {
                System.err.println("Document not found.");
            }
        }
    }

}
