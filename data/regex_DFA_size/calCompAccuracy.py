import re
import csv
import pdb

code_string="code_string.csv"
code_regex="code_regex_python.csv"
#code_regex="code_regex.csv"

dict_regex={} # map from pattern to regex
with open(code_regex,"rb") as infile:
	reader=csv.reader(infile,delimiter="\t")
    	for row in reader:
		key=row[0] #code
		value=row[1] #regex
		dict_regex[key]=value

dict_string={} # map from pattern to regex
##write to csv file dict_string
csv_stat=open("composed_accur.csv",'wb')
writer=csv.writer(csv_stat,quoting=csv.QUOTE_MINIMAL)
with open(code_string,"rb") as infile:
	reader=csv.reader(infile,delimiter="\t")
	regex_str=None
	regex_obj=None
	code=None
	correct=0
	incorrect=0
    	for row in reader:
	#	pdb.set_trace()
		com_code=row[0]
		com_str=row[1]
		com_regex=dict_regex[com_code]
		if com_regex!=regex_str:
			if regex_obj is not None: ##start a new patter matching
				#print com_regex,correct,incorrect
				dict_string[code]=(correct,incorrect)
				writer.writerow([code,correct,incorrect])
			regex_str=com_regex
			code=com_code
			try:
				regex_obj=re.compile(regex_str)
			except:
				print "Error compile regex:",regex_str
				continue
			correct=0
			incorrect=0
		if regex_obj.match(com_str):
			correct+=1
		else:
			incorrect+=1	
		print regex_str,correct,incorrect
	dict_string[code]=(correct,incorrect) ##the last code string
	writer.writerow([code,correct,incorrect])
