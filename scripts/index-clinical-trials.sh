#!/bin/bash
nohup java -cp lib/*:trec2017-1.0-SNAPSHOT.jar at.medunigraz.imi.bst.clinicaltrial.Indexing /home/info/trec/data/clinicaltrials | tee clinicaltrials.log