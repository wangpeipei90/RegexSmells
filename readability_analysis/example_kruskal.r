tbl = read.table("/Users/carlchapman/git/regex_readability_study/readability_analysis/M3R0V0_OCTRNG_M3R0V1_CCC_M3R0V2_HEXRNG.tsv",TRUE,"\t")
results = kruskal.test(value ~ code, data=tbl)
capture.output(results,file="/Users/carlchapman/git/regex_readability_study/readability_analysis/example_kruskal_output.txt")
