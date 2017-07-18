#!/bin/bash

PATH=/mnt/trec/data/extra-abstracts/extracted
#PATH=/home/info/trec/data/extra-abstracts

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.extraabstracts.Indexing" -Dexec.args="$PATH" | tee extra.log