#!/bin/bash

PATH=/mnt/trec/data/medline/extracted
#PATH=/home/info/trec/data/medline/compressed

# Fixes Java heap space error
export MAVEN_OPTS=-Xmx4g

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.medline.Indexing" -Dexec.args="$PATH 1" | tee medline.log