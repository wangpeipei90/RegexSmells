tbl = read.table("/Users/carlchapman/git/regex_readability_study/data/original/COMPLETE_ANOVA_INPUT.csv",TRUE,",")
test_output = aov(accuracy~string*abstract*node,data=tbl)
results=summary(test_output)
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/data/Routput/COMPLETE_ANOVA.Rout")