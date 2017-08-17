from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException as ERROR
from selenium.common.exceptions import ElementNotInteractableException as WHY
from selenium.common.exceptions import NoAlertPresentException as ALERT
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

#actions.send_keys('dummydata')

browser = webdriver.Firefox()
browser.get("http://bg.upf.edu/fannsdb/signin?next=%2Ffannsdb%2Fquery%2Fcondel")
login = browser.find_element_by_id("username")
login.click()

#actions = ActionChains(self.browser)


#for i in range (0,5):
#	login.send_keys(Keys.TAB)

while True:
	try:
		email = browser.find_element_by_xpath("//input[@type='text']")
		break
	except ERROR:
		continue
while True:
	try:
		email.send_keys("pridaproject@gmail.com")
		break
	except WHY:
		continue
while True:
	try:
		password = browser.find_element_by_xpath("//input[@name='password']")
		password.send_keys("sirdancealot")
		login = browser.find_element_by_xpath("//button[@type='submit']")
		login.click()
		break	
	except WHY:
		continue

while True:
	try:
		browser.switch_to_alert().accept()
		break
	except ALERT:
		continue


while True:
	try:
		mutation_area = browser.find_element_by_partial_link_text("load mutations" )
		mutation_area.click()
		mutation_area.send_keys("/home/mrek/helomalatya.vcf")
		break
	except ERROR:
		continue

name = browser.find_element_by_xpath("//input[@id='project_name']")
name.send_keys("PRIDA")

query = browser.find_element_by_xpath("//button[@type='submit']")
query.c1ick()




