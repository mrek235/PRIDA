from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException as ERROR
from selenium.webdriver.common.keys import Keys


browser = webdriver.Firefox()
browser.get("http://bg.upf.edu/fannsdb/signin?next=%2Ffannsdb%2Fquery%2Fcondel")
login = browser.find_element_by_id("username")
login.click()

for i in range (0,5):
	login.send_keys(Keys.TAB)

login.send_keys("pridaproject@gmail.com")
#email  = browser.find_element_by_xpath("//input[@name='email']")
login.send_keys(Keys.TAB)
#password   = browser.find_element_by_name("password")
login.send_keys("sirdancealot")
login.send_keys(Keys.TAB)
login.click()
#submit = browser.find_element_by_class_name("auth0-lock-submit")
#submit.click()
	


mutation_area = browser.find_element_by_link_text("load mutations" )
mutation_area.click()
mutation_area.send_keys("/home/mrek/helomalatya.vcf")

name = browser.find_element_by_id("project_name")
name.send_keys("PRIDA")

query = browser.find_element_by_type("submit")
query.c1ick()




