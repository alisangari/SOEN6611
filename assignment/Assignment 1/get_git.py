#!/usr/bin/python

# The following two modules are needed for this script.
# This script is written for Python 3.4
#pip install beautifulsoup4
#pip install gitpython

from urllib.request import urlopen
from bs4 import BeautifulSoup
import subprocess
import os
import re
import git
import sys



def readFile(configFile):
    fo = open(configFile, "r")

    url = fo.readline()
    url = url[url.find("=")+1:]
    url = url.strip()
    print ("Read Line: %s" % (url))

    root_directory = fo.readline()
    root_directory = root_directory[root_directory.find("=")+1:]
    root_directory = root_directory.strip()
    print ("Read Line: %s" % (root_directory))

    fo.close()
    return {'url' : url, 'root_directory' : root_directory}

    

def readHTMLPageSource(url):
	response = urlopen(url)
	page_source = response.read()
	codec = response.info().get_param('charset', 'utf8')
	page_source = page_source.decode(codec)
	return page_source

	
def getAllLinks (pageSource):
	soup = BeautifulSoup(pageSource)
	linksList = [url]
	for link in soup.findAll("a"):
		href = link.get("href")
		if(href.startswith("https://")):
			linksList.append(href)
	return linksList

	
def getCleanLinks (linksList):
	cleanLinksList = []
	for link in linksList:
		#if (not link.find('?') and not link.find('#'))):
		pos = link.rfind('/')
		last_part = link[pos+1:]
		if(last_part.find('.git')): 
			cleanLinksList.append(link)
		if (not last_part.find('.')):
			cleanLinksList.append(link+'.git')
	return cleanLinksList

	
def getCleanLinksDirectory (linksList):
	cleanLinksListDirectory = []
	for link in linksList:
		pos = link.rfind('/')
		last_part = link[pos+1:]
		if(last_part.find('.git')): 
			cleanLinksListDirectory.append(last_part[:len(last_part)-4])
		if (not last_part.find('.')):
			cleanLinksListDirectory.append(last_part)
	return cleanLinksListDirectory
	
	
def cloneGitRepo(link):
	git.Git().clone(link)
	
	

#"git_conf.txt"	
#print (str(sys.argv[1]))
url_rootDir = readFile(str(sys.argv[1]))

url = url_rootDir['url']
root_directory = url_rootDir['root_directory']

htmlPageSource = readHTMLPageSource(url)

linksList = getAllLinks(htmlPageSource)
cleanLinksList = getCleanLinks(linksList)
directories = getCleanLinksDirectory(linksList)

print ('Please wait...')
for link in cleanLinksList:
	try:
		cloneGitRepo(link)
		print ('-- ', link, 'is downloaded.')
		print ('Please wait...')

	except:
		n = 1
		
print("Cloning is complete!")
