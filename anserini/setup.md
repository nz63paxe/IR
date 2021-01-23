# Anserini

## Setup

Clone repo (or pull current version)

Save all 5 json files (idebate.json, parliamentary.json, ...) in advanced-information-retrieval/data/

Navigate to anserini folder & build project

```
cd anserini
mvn clean package appassembler:assemble -DskipTests -Dmaven.javadoc.skip=true
```

Build index

```
mvn exec:java -Dexec.mainClass="robinhood.IndexArguments"
mvn exec:java -Dexec.mainClass="robinhood.IndexArgumentsConcatenated"
```

Search (put query as argument)
```
mvn exec:java -Dexec.mainClass="robinhood.BaselineSearcher" -Dexec.args="Should guns be banned?"
```

## Anserini (not needed)

First, install Maven (Java 11 comes pre-installed already):

```
apt-get install maven -qq
```

Clone and build Anserini:

```
git clone --recurse-submodules https://github.com/castorini/anserini.git
cd anserini
cd tools/eval && tar xvfz trec_eval.9.0.4.tar.gz && cd trec_eval.9.0.4 && make && cd ../../..
mvn clean package appassembler:assemble -DskipTests -Dmaven.javadoc.skip=true
```

If all goes well, you should be able to see  anserini-X.Y.Z-SNAPSHOT-fatjar.jar in target/:

```
ls target
```
