from urllib.request import urlopen
from bs4 import BeautifulSoup
import subprocess
import os
import re
import git

def readFile():
    fo = open("git_conf.txt", "r")

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
	linksList =[url]
	for link in soup.findAll("a"):
		href = link.get("href")
		if(href.startswith("https://")):
			linksList.append(href)
	return linksList

	
#def validateGitRepo(linksList):
#	for link in linksList:
#		if (link)
	
def getGitUrl(linksList):	
	match = re.search(r'https://github.com/.*git', pageSource)
	if match:
		gitRepo = match.group()
		directory = gitRepo[gitRepo.find("github.com/")+11:]
		#print (directory)
		directory = directory[directory.find("/")+1:]
		#print (directory)
		directory = directory[:directory.find(".git")]
		#print (directory)
		return {'gitRepo' : match.group(), 'directory': directory} 	
	return {'gitRepo' : "", 'directory': ""}
	

def cloneGitRepo(repoPath, root_directory):
	subprocess.Popen(['git', 'clone', str(repoPath), root_directory+'/'])

# main program

#url_rootDir = readFile()

#url = url_rootDir['url']
#root_directory = url_rootDir['root_directory']

#htmlPageSource = readHTMLPageSource(url)

#linksList = getAllLinks(htmlPageSource)
#repo = getGitUrl(htmlPageSource)

print (":::::::::::::::::::::::")
#repo = git.Repo.cloneFrom('https://github.com/Grarak/KernelAdiutor')
git.Git().clone("https://github.com/Grarak/KernelAdiutor")

#print (repo.git.status())

#for link in linksList:
#	cloneGitRepo(link, root_directory+repo['directory'])
#cloneGitRepo(repo['gitRepo'], root_directory+repo['directory'])