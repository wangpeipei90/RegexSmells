tbl = read.table("/Users/carlchapman/git/regex_readability_study/readability_analysis/M0R0V0_CC_SEQ_M0R0V1_CC_SNG.tsv",TRUE)
results = wilcox.test(tbl[,1],tbl[,2])
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/readability_analysis/example_R_output.txt")
