package robinhood;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

/**
 * Searcher müssen dieses Interface implementieren um evaluierbar zu sein.
 */
public interface ISearcher {

    /**
     * Gibt den Namen des Searchers zurück. Unter diesem Namen (+ Versionsnummer) werden die Ergebnisse der Evaluation angezeigt.
     *
     * @return String
     */
    public String getName();

    /**
     * Gibt die Version des Searchers zurück. Unter diesem Nummer (+ Name) werden die Ergebnisse der Evaluation angezeigt.
     *
     * @return int
     */
    public double getVersionNumber();

    /**
     * Gibt den verwendeten IndexSearcher zurück. Wird für Evaluation benötigt.
     *
     * @return IndexSearcher
     */
    public IndexSearcher getIndexSearcher();

    /**
     * Die eigentliche Suchfunktion. Erhält eine Query und gibt die top x Ergebnisse zurück.
     *
     * @param query String - die Eingabe des Nutzers
     * @param maxAmount int - die maximale Anzahl an zu suchenden Argumenten
     * @return TopDocs - gefundene Dokumente in der richtigen Reihenfolge
     * @throws Exception
     */
    public TopDocs search(String query, int maxAmount) throws Exception;

}
