package robinhood;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.*;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;
import org.apache.jute.Index;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.util.fst.Builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PseudoRelevanceSearcher extends AbstractSearcher implements ISearcher {

    private String name = "PseudoRelevance";
    private double versionNumber = 1.0;
    private IndexSearcher indexSearcher;
    private Synonyms synonyms;

    public static void main(String[] args) throws Exception { // mvn exec:java -Dexec.mainClass="robinhood.PseudoRelevanceSearcher" -Dexec.args="Should guns be banned?"
        PseudoRelevanceSearcher searcher = new PseudoRelevanceSearcher();
        String query = args[0];
        searcher.printResults(searcher.search(query.toLowerCase(), 10), searcher.getIndexSearcher());
    }

    public PseudoRelevanceSearcher() throws Exception {
        indexSearcher = createSearcher(new LMDirichletSimilarity(), Variables.concatenatedArgumentIndexPath);
        synonyms = new Synonyms();
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
        BaselineSearcher baselineSearcher = new BaselineSearcher();
        TopDocs foundDocs = baselineSearcher.search(query, 10);
        String[] words = query.replaceAll("[^A-Za-z0-9 ]", "").split(" ");
        Map<String, Set<String>> querySynonyms = new HashMap<>();
        for (String w : words) {
            Set<String> syn = synonyms.getSynonyms(w);
            querySynonyms.put(w, syn);
        }
        Map<String, Set<String>> querySynonymsFiltered = new HashMap<>();
        for (ScoreDoc sd : foundDocs.scoreDocs) {
            Document d = indexSearcher.doc(sd.doc);
            String argument = d.get("fullArgument");
            for (String w : querySynonyms.keySet()) {
                Set<String> wSyns = new HashSet<>();
                if (querySynonymsFiltered.containsKey(w)) {
                    wSyns = querySynonymsFiltered.get(w);
                }
                for (String s : querySynonyms.get(w)) {
                    if (argument.contains(s)) {
                        wSyns.add(s);
                    }
                }
                querySynonymsFiltered.put(w, wSyns);
            }
        }
        String[] queries = new String[words.length]; // querySynonymsFiltered.keySet().size()
        for (int i = 0; i < words.length; i++) {
            String s = "(" + words[i].toLowerCase();
            for (String syn : querySynonymsFiltered.get(words[i])) {
                s += " OR " + syn.toLowerCase() + "^0.9";
            }
            s += ")";
            queries[i] = s;
        }
        String combinedQuery = "";
        for (String s : queries) {
            combinedQuery += s + " ";
        }
        TopDocs foundDocsNew = baselineSearcher.search(combinedQuery, 10);
        return  foundDocsNew;
    }

}
