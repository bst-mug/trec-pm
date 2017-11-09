setwd("~/git/bst-mug/trec2017/paper/provisional-results")

library(dplyr)
library(ggplot2)

run_ids <- c('mugpubboost', 'mugpubshould', 'mugpubbase', 'mugpubdiseas', 'mugpubgene')

results <- read.table(paste("scientific_abstracts/", run_ids[1], ".trec_eval", sep=""), header = FALSE)
results <- results %>% mutate(run=run_ids[1])
names(results) <- c('measure', 'topic', 'value', 'run')
tmp <- read.table(paste("scientific_abstracts/", run_ids[1], ".sampleval", sep=""), header = FALSE)
tmp <- tmp %>% mutate(run_ids[1])
names(tmp) <- c('measure', 'topic', 'value', 'run')
results <- results %>% bind_rows(tmp)
remove(tmp)

for(run_id in run_ids[seq(2, length(run_ids))]) {
    tmp <- read.table(paste("scientific_abstracts/", run_id, ".trec_eval", sep=""), header = FALSE)
    tmp <- tmp %>% mutate(run_id) 
    names(tmp) <- c('measure', 'topic', 'value', 'run')
    results <- results %>%  bind_rows(tmp)
    remove(tmp)
    tmp <- read.table(paste("scientific_abstracts/", run_id, ".sampleval", sep=""), header = FALSE)
    tmp <- tmp %>% mutate(run_id) 
    names(tmp) <- c('measure', 'topic', 'value', 'run')
    results <- results %>%  bind_rows(tmp)
    remove(tmp)
}

# Fix as factors for printing correctly
results$run <- factor(results$run, levels=c('mugpubboost', 'mugpubshould', 'mugpubbase', 'mugpubdiseas', 'mugpubgene'))

test <- results %>% filter(measure=='infNDCG',topic!='all')

best <- 0.5872
means <- aggregate(value ~ run, test, mean)

g <- ggplot(test, aes(x=run, y=value, fill=run)) +
    geom_boxplot() + 
    stat_summary(fun.y=mean, color="red", geom="point", shape=18, size=3,show.legend = FALSE) + 
    geom_text(data = means, aes(label = round(value,4), y = value + 0.04)) +
    guides(fill=FALSE) + scale_y_continuous(breaks=seq(0,1,0.1), limits=c(0,1))

g + geom_hline(yintercept=best, linetype="dashed", color = "red") +
    geom_text(aes(2.5,best,label="best", vjust = -1), color="red")



