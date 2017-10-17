setwd("/home/peipei/RegexSmells/data/regex_DFA_size")

regex_str="/home/peipei/RegexSmells/data/regex_DFA_size/regex.csv" ##stats from crowd sourcing results
strs=read.csv(file=regex_str,head=TRUE,colClasses=c("numeric","numeric","character","character","character","numeric"),sep=",")
strs=as.data.frame(strs)

which(strs$String1!=strs$String2) ##check modified columns
ssc=substr(strs$String,1,nchar(strs$String)-1) ##remove last single quotation from original string
mm=which(ssc!=strs$String1) ### check if modified is equal to original
strs[mm,c("String","String1","String2")] ### show dirty regexes

##get unique regex strings
unique_regex=unique(strs$String1)
length(unique_regex)
write(unique_regex, file = "unique_regex", ncolumns = if(is.character(unique_regex)) 1 else 5, append = FALSE, sep = " ")
