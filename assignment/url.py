from bs4 import BeautifulSoup
soup = BeautifulSoup("http://google.com")
for link in soup.findAll("a"):
    print (link)