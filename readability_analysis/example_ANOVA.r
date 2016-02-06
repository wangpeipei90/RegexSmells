tbl = read.table("/Users/carlchapman/git/regex_readability_study/readability_analysis/M0.csv",TRUE,",")
results = kruskal.test(value ~ code, data=tbl)
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/readability_analysis/MO_output.txt")
