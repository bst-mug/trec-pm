#!/bin/bash
nohup java -cp lib/*:trec2017-1.0-SNAPSHOT.jar at.medunigraz.imi.bst.medline.Indexing /home/info/trec/data/medline/compressed 1 | tee medline.log