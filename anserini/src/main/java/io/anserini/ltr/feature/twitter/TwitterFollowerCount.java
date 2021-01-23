/*
 * Anserini: A Lucene toolkit for replicable information retrieval research
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.anserini.ltr.feature.twitter;

import io.anserini.index.IndexArgs;
import io.anserini.index.generator.TweetGenerator.TweetField;
import io.anserini.ltr.feature.FeatureExtractor;
import io.anserini.rerank.RerankerContext;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;

import java.util.List;

public class TwitterFollowerCount implements FeatureExtractor {
  @Override
  public float extract(Document doc, Terms terms, String queryText, List<String> queryTokens, IndexReader reader) {
    return (float) (int) doc.getField(TweetField.FOLLOWERS_COUNT.name).numericValue();
  }

  @Override
  public String getName() {
    return "TwitterFollowerCount";
  }

  @Override
  public String getField() {
    return TweetField.FOLLOWERS_COUNT.name;
  }

  @Override
  public FeatureExtractor clone() {
    return new TwitterFollowerCount();
  }
}