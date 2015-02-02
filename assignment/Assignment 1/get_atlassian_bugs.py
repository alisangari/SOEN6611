from urllib.request import urlopen
import subprocess
import os
import re
import random
from random import randint
from time import sleep
import sys

def readFile(configFile):
    fo = open(configFile, "r")

    url = fo.readline()
    url = url[url.find("=")+1:]
    url = url.strip()
    print ("Read Line: %s" % (url))

    project_tag = fo.readline()
    project_tag = project_tag[project_tag.find("=")+1:]
    project_tag = project_tag.strip()
    print ("Read Line: %s" % (project_tag))
	
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
	'project_tag' : project_tag,
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
	print(fileName)
	with open(fileName, "wt") as out_file:
		out_file.write(content)
		out_file.close()
	
# main program
	

configs = readFile(str(sys.argv[1]))

url = configs['url']
project_tag = configs['project_tag']
bug_start = int(configs['bug_start'])
bug_end = int(configs['bug_end'])+1
max_timeout_secs = configs['max_timeout_secs']
root_directory = configs['root_directory']

if not os.path.exists(root_directory+project_tag):
	os.makedirs(root_directory+project_tag)
	
for bugNum in range(bug_start, bug_end):
	htmlPageSource = readHTMLPageSource(url + project_tag + "-" + str(bugNum))
	saveBugToDisk(root_directory + project_tag + "-" + str(bugNum) + ".html", htmlPageSource)
	randWait = random.randrange(1, int(max_timeout_secs))
	print (randWait)
	sleep(randint(1,randWait))
	
	
#end