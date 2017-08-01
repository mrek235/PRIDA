import urlparse
import urllib2
import os
import sys
import requests

try:
	from bs4 import BeautifulSoup
except ImportError:
	print "Please download and install Beautiful Soup!"
	sys.exit(0)
try:
	from selenium import webdriver
	from selenium.common.exceptions import TimeoutException
	from selenium.webdriver.support import expected_conditions as EC 
	from selenium.common.exceptions import NoSuchElementException as ERROR 
except ImportError:
	print "Please download and install Selenium"
	sys.exit(0)


browser = webdriver.Firefox()
browser.get("http://www.mutationtaster.org/StartQueryEngine.html")


mahmut = browser.find_element_by_xpath("//input[@type='file']")
mahmut.click()
mahmut.send_keys("/home/mrek/helomalatya.vcf")

name   = browser.find_element_by_name("name")
email  = browser.find_element_by_name("email")
submit = browser.find_element_by_name("Submit")
name.send_keys("PRIDA")
email.send_keys("ogulcan.cingiler@ozu.edu.tr")

homozygous_TGP_Box = browser.find_element_by_name("tgp_filter_homo")
homozygous_TGP_Box.click()

minimum_coverage = browser.find_element_by_name("min_cov")
minimum_coverage.clear()
minimum_coverage.send_keys("0")

submit.click()


while True: 
	try:
		tuples = browser.find_element_by_name("tuples")
		tuples.clear()
		tuples.send_keys("1000000") 
		break
	except ERROR:
		continue

export_TSV = browser.find_element_by_xpath("//input[@value='export as TSV']")
export_TSV.click()

while True:
	try:	
		# retrieve_TSV = browser.find_element_by_link_text("retrieve TSV file")
		#retrieve_TSV.click()
		url = browser.current_url
		#urllib.urlretrieve(url,"export.zip")
		r = requests.get(url)
		with open("export.zip","wb") as code:
			code.write(r.content)
		break
	except ERROR:
		continue
