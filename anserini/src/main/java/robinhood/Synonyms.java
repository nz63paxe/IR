package robinhood;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Synonyms {

    private Dictionary dict;

    public Synonyms() throws Exception {
        dict = Dictionary.getDefaultResourceInstance();
    }

    public Set<String> getSynonyms(String word) throws JWNLException {
        Set<String> synonyms = new HashSet<>();
        IndexWordSet indexWordSet = dict.lookupAllIndexWords(word);
        for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
            for (Synset synset : indexWord.getSenses()) {
                for (Word wordObj : synset.getWords()) {
                    synonyms.add(wordObj.getLemma());
                }
            }
        }
        synonyms.remove(word);
        return synonyms;
    }

}
