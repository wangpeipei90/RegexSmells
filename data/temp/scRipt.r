tbl = read.table("/Users/carlchapman/git/regex_readability_study/data/Rinput/composition/M/M9.csv",TRUE,",")
test_output = aov(accuracy~regex*refactoring,data=tbl)
results=summary(test_output)
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/data/Routput/composition/M/M9.Rout")