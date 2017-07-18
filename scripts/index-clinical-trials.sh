#!/bin/bash

PATH=/mnt/trec/data/clinicaltrials/extracted
#PATH=/home/info/trec/data/clinicaltrials

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.clinicaltrial.Indexing" -Dexec.args="$PATH" | tee clinicaltrials.log