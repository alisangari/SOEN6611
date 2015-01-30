from urllib.request import urlopen
import subprocess
import os
import re

def readFile():
    fo = open("bugs_conf.txt", "r")

    url = fo.readline()
    url = url[url.find("=")+1:]
    url = url.strip()
    print ("Read Line: %s" % (url))

    bug_start = fo.readline()
    bug_start = bug_start[bug_start.find("=")+1:]
    bug_start = bug_start.strip()
    print ("Read Line: %s" % (bug_start))

    bug_end = fo.readline()
    bug_end = bug_end[bug_end.find("=")+1:]
    bug_end = bug_end.strip()
    print ("Read Line: %s" % (bug_end))

    max_timeout_secs = fo.readline()
    max_timeout_secs = max_timeout_secs[max_timeout_secs.find("=")+1:]
    max_timeout_secs = max_timeout_secs.strip()
    print ("Read Line: %s" % (max_timeout_secs))

    root_directory = fo.readline()
    root_directory = root_directory[root_directory.find("=")+1:]
    root_directory = root_directory.strip()
    print ("Read Line: %s" % (root_directory))

    fo.close()
    return {'url' : url, 
	'bug_start' : bug_start, 
	'bug_end' : bug_end, 
	'max_timeout_secs' : max_timeout_secs, 
	'root_directory' : root_directory}

    

def readHTMLPageSource(url):
	print (url)
	response = urlopen(url)
	page_source = response.read()
	codec = response.info().get_param('charset', 'utf8')
	page_source = page_source.decode(codec)
	return page_source
	
def saveBugToDisk(fileName, content):
	#print(fileName)
	fo = open(fileName, "w")
	fo.write(content)
	fo.close()

# main program

configs = readFile()

url = configs['url']
bug_start = int(configs['bug_start'])
bug_end = int(configs['bug_end'])+1
max_timeout_secs = configs['max_timeout_secs']
root_directory = configs['root_directory']
for bugNum in range(bug_start, bug_end):
	htmlPageSource = readHTMLPageSource("https://hibernate.atlassian.net/browse/WEBSITE-"+ str(bugNum))
	saveBugToDisk(root_directory + "WEBSITE-" + str(bugNum) + ".html", htmlPageSource)

#end