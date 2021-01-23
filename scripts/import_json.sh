cd ../data
for z in *.zip; do
    unzip $z
done

mkdir splits
cd ../scripts
python json_for_elasticsearch.py
cd ../data
for j in *.json; do
    rm $j
done

cd splits
for f in *.json; do
    curl -H "Content-Type: application/json" -XPOST "localhost:9200/arguments/argument/_bulk?pretty&refresh" --data-binary "@$f"
    rm $f
done

curl "localhost:9200/_cat/indices?v"

