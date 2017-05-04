setwd("/home/peipei/CodeSmell_ICPC/data/regex_DFA_size")

regex_accuracy="regex.csv" ##stats from crowd sourcing results
regex_unique_dfa="uniregex_dfa_size.csv"
regex_comp_dfa="regex_dfa_comp.csv"
regex_pattern_map_type="pattern_map_type.csv"

accur=read.csv(file=regex_accuracy,head=TRUE,
               colClasses=c("numeric","numeric","character","character","character","numeric"),sep=",")
accur=as.data.frame(accur)
accur=accur[,c("UniqueID","HIT_ID","String1","nCorrect_over_nAnswered")]

size_dfa=read.csv(file=regex_unique_dfa,head=TRUE,
                  colClasses = c("character","numeric","numeric","character"),sep=",")
regex_comp=read.csv(file=regex_comp_dfa,head=TRUE,sep=",",
                  colClasses = c("character","character","numeric","numeric","character","character","numeric","character","character"))
regex_map=read.csv(file=regex_pattern_map_type,head=TRUE,sep=",",colClasses = c("character","character","character","character"))


#split mapped types into separate rows for regex_map
s1=strsplit(regex_map$P_CR1,split=", ")
s2=strsplit(regex_map$P_CR2,split=", ")
rep1=sapply(s1,length)
rep2=sapply(s2,length)
type_map=data.frame(P_CR1 = unlist(s1), p_CR2= unlist(s2),CR1 = rep(regex_map$CR1, rep1), CR2 = rep(regex_map$CR2,rep2))

##add DFA sizes for each regex comparsion

ttype_map=merge(type_map,regex_comp,by=c("CR1","CR2"))