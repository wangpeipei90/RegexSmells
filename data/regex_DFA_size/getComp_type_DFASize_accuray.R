setwd("/home/peipei/CodeSmell_ICPC/data/regex_DFA_size")

regex_accuracy="regex.csv" ##stats from crowd sourcing results
regex_unique_dfa="uniregex_dfa_size.csv"
regex_comp_dfa="regex_dfa_comp.csv"
regex_pattern_map_type="pattern_map_type.csv"
regex_composed="composition.csv"
code_accuracy="composed_accur.csv"

accur=read.csv(file=regex_accuracy,head=TRUE,
               colClasses=c("integer","integer","character","character","character","numeric"),sep=",")
accur=as.data.frame(accur)
accur=accur[,c("UniqueID","HIT_ID","String1","nCorrect_over_nAnswered")]
names(accur)=c("UniqueID","HIT_ID","regex","accur")
##show the rows of NA accuracy
which(is.na(accur$accur))
##get average accuracy for each regex string remove the record which accuracy is NA
avg_accur=sapply(split(accur,f=accur$regex),function(x) mean(x$accur,na.rm=TRUE))


size_dfa=read.csv(file=regex_unique_dfa,head=TRUE,
                  colClasses = c("character","integer","integer","character"),sep=",")
size_dfa=as.data.frame(size_dfa)

regex_comp=read.csv(file=regex_comp_dfa,head=TRUE,sep=",",
                  colClasses = c("character","character","integer","integer","character","character","integer","character","character"))
regex_comp=as.data.frame(regex_comp)


##recompose regex_dfa according to comp
regex_comp=regex_comp[,c("CR1","CR2","dfa_size")]
regex_dfa=melt(regex_comp,id=c("dfa_size"))
regex_dfa=regex_dfa[,c("dfa_size","value")]
regex_dfa=unique(regex_dfa)
names(regex_dfa)=c("dfa_size","regex")

##add accuracy comparison between regex strings 
regex_comp$Accur1=avg_accur[regex_comp$CR1]
regex_comp$Accur2=avg_accur[regex_comp$CR2]

#add accuracy and length to each dfa and regex
regex_dfa$AvgAccur=avg_accur[regex_dfa$regex]
regex_dfa$str_len=sapply(regex_dfa$regex,nchar)

getDFASize=function(x){
  s=regex_dfa[which(regex_dfa$regex==x),"dfa_size"]
  return(s)
}
accur$dfa_size=sapply(accur$regex,getDFASize)
accur$str_len=sapply(accur$regex,nchar)
accur=accur[-which(is.na(accur$accur)),] ##remove rows whose accuracy is NA

##get range of dfa size
table(accur$dfa_size)
unique(accur$dfa_size) ##2,3,4,5,6,7,8 [2-8]
##get range of regex size
table(accur$str_len)
unique(accur$str_len) ##[2-19] [22,23,25,26]

##data=regex_dfa anova analysis and correlation with average accur
fit=aov(AvgAccur~dfa_size*str_len,data=regex_dfa)
summary(fit)
cor.test(regex_dfa$dfa_size,regex_dfa$AvgAccur,method="spearman") ##[0.1904738,0.1449]
cor.test(regex_dfa$str_len,regex_dfa$AvgAccur,method="spearman") ##[-0.04336877,0.7421]

fit1=aov(AvgAccur~as.factor(dfa_size)*as.factor(str_len),data=regex_dfa)
summary(fit1)
##get factors of dfa_size and str_len----same dfa_size, same str_len
level_dfa_size=as.factor(accur$dfa_size)
level_str_len=as.factor(accur$str_len)

##data=accur aov analysis of variance regarding dfa size as continuous, and dfa_size as discrete, both dfa_size and str_len as discrete
fit1=aov(accur ~ dfa_size*str_len, data=accur)
summary(fit1)
fit2=aov(accur~level_dfa_size*str_len, data=accur)
summary(fit2)
fit3=aov(accur~level_dfa_size*level_str_len, data=accur)
summary(fit3)

##correlation---non-parametric method spearman  as integer, could not use as factor
cor.test(accur$dfa_size,accur$accur,method="spearman") ##[0.0706446,0.002958]
cor.test(accur$str_len,accur$accur,method="spearman") ##[-0.02540443,0.2857]
#cor.test(accur$dfa_size,accur$accur,method="kendall") ##[0.05791798,0.003139]
#cor.test(accur$str_len,accur$accur,method="kendall") ##[-0.01988941,0.2857]

###relation--accur~(regex_str_len, regex_dfa_size)------real data relationship
tt=split(regex_dfa, with(regex_dfa, interaction(dfa_size,str_len)),drop = TRUE)
tt=sapply(tt,function(x)mean(x$AvgAccur))



regex_map=read.csv(file=regex_pattern_map_type,head=TRUE,sep=",",colClasses = c("character","character","character","character","character","character"))
regex_map=as.data.frame(regex_map)

##extract regex_presentation
r1=regex_map[,c("code1","CR1","P_CR1")]
names(r1)=c("code","regex","prep")
r2=regex_map[,c("code2","CR2","P_CR2")]
names(r2)=c("code","regex","prep")
regex_prep=unique(rbind(r1,r2))
##get code_regex and write to file
code_regex=unique(regex_prep[,c("code","regex")])
code_regex=code_regex[order(code_regex$code),]
write.table(code_regex, file = "code_regex.csv",row.names=FALSE, na="",col.names=FALSE, sep="\t")

t1=regex_prep$regex #65
t2=regex_dfa$regex #60
##5 regex have 2 representation
tt=split(regex_prep,regex_prep$regex)
which(sapply(tt,nrow)>1)
tt[which(sapply(tt,nrow)>1)]
tt[which(sapply(tt,nrow)>1)]
# $`(:|;)`
# code regex prep
# 22 M6R1V0_OR (:|;)   C5
# 23 M6R1V0_OR (:|;)   T1
# $`([:;])`
# code  regex prep
# 24 M6R1V1_CCC ([:;])  T1 
# 64 M6R1V1_CCC ([:;])   C4
# $`([}{])`
# code  regex prep
# 21 M6R0V1_CCC ([}{])  T1 
# 61 M6R0V1_CCC ([}{])   C4
# $`(\\{|\\})`
# code     regex prep
# 19 M6R0V0_OR (\\{|\\})   C5
# 20 M6R0V0_OR (\\{|\\})   T1
# $`no(w|x|y|z)5`
# code        regex prep
# 59 M5R1V2_OR no(w|x|y|z)5   C5
# 60 M5R1V2_OR no(w|x|y|z)5   C3
d1=c("23","24","21","20","59") ##delete T
d2=c("22","64","61","19","60") ##delete C
d2=which(rownames(regex_prep) %in% d2)
d1=which(rownames(regex_prep) %in% d1)

regex_prep=regex_prep[-d2,] ##60
##append prep to accur and regex_dfa
getRegexPrep=function(x){
  return(regex_prep[which(regex_prep$regex==x),"prep"])
}

accur$prep=sapply(accur$regex,getRegexPrep)
regex_dfa$prep=sapply(regex_dfa$regex,getRegexPrep)

fit <- aov(accur ~ dfa_size*str_len*prep, data=accur)
summary(fit)
fit1 <- aov(AvgAccur ~ dfa_size*str_len*prep, data=regex_dfa)
summary(fit1)


##handle composition.csv
str_compose=read.csv(file=regex_composed,head=FALSE,sep=",",colClasses = rep("character",20))
cnames=colnames(str_compose)
getSub=function(i,composed){
  sub=str_compose[,cnames[c(i,i+1)]]
  colnames(sub)=c("code","string")
  return(rbind(composed,sub))
}
composed=str_compose[,cnames[c(1,2)]]
colnames(composed)=c("code","string")
for (i in c(3,5,7,9,11,13,15,17,19)){
  composed=getSub(i,composed)
}
##order by code
composed=composed[order(composed$code),]
##remove trailing '
getRows=which(grepl("*'$",composed$string))
for (r in getRows){
  org_str=composed[r,"string"]
  composed[r,"string"]=substr(org_str,1,nchar(org_str)-1)
}
write.table(composed, file = "code_string.csv",row.names=FALSE, na="",col.names=FALSE, sep="\t")

##get code_accurracy
accur_compose=read.csv(file=code_accuracy,head=FALSE,sep=",",colClasses = c("character","integer","integer"))
names(accur_compose)=c("code","correct","incorrect")
accur_compose$CompAccuracy=accur_compose$correct*100.00/(accur_compose$correct+accur_compose$incorrect)
getRegexComposedAccur=function(x){ ##x is regex
    rcode=regex_prep[which(regex_prep$regex==x),"code"]
    return(accur_compose[which(accur_compose$code==rcode),"CompAccuracy"])
}

regex_dfa$CompAccur=sapply(regex_dfa$regex,getRegexComposedAccur)
##drop invalid comparison P_CR1 P_CR2 46, 56
#size_dfa[46,"regex"]
#[1] "\\..*"
#> size_dfa[56,"regex"]
#[1] "\\.+"
##row 37 in regex_map
dropline=which(regex_map$CR1=="\\..*")
regex_map=regex_map[-dropline,]

# #split mapped types into separate rows for regex_map  ##not necessary if P_CR1 and P_CR2 have only two represnetaions
# s1=strsplit(regex_map$P_CR1,split=", ")
# s2=strsplit(regex_map$P_CR2,split=", ")
# s1=sapply(s1,function(x)trimws(x,which="both"))
# s2=sapply(s2,function(x)trimws(x,which="both"))
# rep1=sapply(s1,length)
# rep2=sapply(s2,length)
# 
# type_map=data.frame(P_CR1 = unlist(s1), P_CR2= unlist(s2), CR1 = rep(regex_map$CR1, rep1), CR2 = rep(regex_map$CR2,rep2))

##add AvgAccur and CompAccur to regx_map
getRegexCompAccur=function(x){ ##x is regex
  compAccur=regex_dfa[which(regex_dfa$regex==x),"CompAccur"]
  return(compAccur)
}
getRegexAvgAccur=function(x){ ##x is regex
  avgAccur=regex_dfa[which(regex_dfa$regex==x),"AvgAccur"]
  return(avgAccur)
}
getRegexCorrect=function(x){ ##x is code
  correct=accur_compose[which(accur_compose$code==x),"correct"]
  return(correct)
}
regex_map$AvgAccur1=sapply(regex_map$CR1,getRegexAvgAccur)
regex_map$AvgAccur2=sapply(regex_map$CR2,getRegexAvgAccur)
regex_map$CompAccur1=sapply(regex_map$CR1,getRegexCompAccur)
regex_map$CompAccur2=sapply(regex_map$CR2,getRegexCompAccur)
regex_map$correct1=sapply(regex_map$code1,getRegexCorrect)
regex_map$correct2=sapply(regex_map$code2,getRegexCorrect)
#do Prop test
doPropTest=function(x){ #row
  prop.test(c(x["correct1"],x["correct2"]),c(30,30))
  print(t$p.value)
  return(t$p.value)
}
regex_map$prop_test=apply(regex_map,1,doPropTest)
# 
# ##combine type comparison (type_map) with dfa size and accuracy(regex_comp) so that we can compare the accuracies for each type comp
# type_map=merge(type_map,regex_comp,by=c("CR1","CR2"))
# 
# ##subtract type_map to contain only type and accuracy, and order P_CR1 and P_CR2 in order
# type_map=type_map[,c("P_CR1","P_CR2","Accur1","Accur2")]
# order_map=function(x){
#   if(x["P_CR1"]>x["P_CR2"]){
#     x[c("P_CR1","P_CR2","Accur1","Accur2")]=x[c("P_CR2","P_CR1","Accur2","Accur1")]
#   }
#   return(x)
# }
# type_map=t(apply(type_map,1,order_map)) ## if P1>P2, switch P1 and P2, swith A1 and A2
# type_map=as.data.frame(type_map)
# type_map$Accur1=as.numeric(as.character(type_map$Accur1))
# type_map$Accur2=as.numeric(as.character(type_map$Accur2))
# type_map$P_CR1=as.character(type_map$P_CR1)
# type_map$P_CR2=as.character(type_map$P_CR2)
# ##get preference and form preference group
# type_map$prefer=type_map$Accur1>type_map$Accur2 ## get preference TRUE prefer P_CR1

##split regex_map by type comparison P_CR1:P_CR2
output=split(regex_map, with(regex_map, interaction(P_CR1,P_CR2)),drop = TRUE)
names(output) 
length(output)##24
sapply(output,nrow)
sum(sapply(output,nrow))==nrow(regex_map)
#make sure the total rows are equal

##get only preference over P_CR1, P_CR2
sapply(output, function(x) x$prefer)
