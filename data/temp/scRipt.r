tbl = read.table("/Users/carlchapman/git/regex_readability_study/data/Rinput/E/E14_compose.tsv",TRUE,"\t")
results = wilcox.test(tbl[,1],tbl[,2])
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/data/Routput/E/E14_compose.Rout")