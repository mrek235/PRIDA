from selenium import webdriver
from selenium.common.exceptions import TimeoutException

browser = webdriver.Firefox()
browser.get("http://bg.upf.edu/fannsdb/signin?next=%2Ffannsdb%2Fquery%2Fcondel")
login = browser.find_element_by_class("btn persona-button")
login.click()
email  = browser.find_element_by_name("email")
email.send_keys("ogulcan.cingiler@ozu.edu.tr")
password   = browser.find_element_by_name("password")
submit = browser.find_element_by_class_name("auth0-lock-submit")

mutation_area = browser.find_element_by_link_text("load mutations" )
mutation_area.click()
mutation_area.send_keys("/home/mrek/helomalatya.vcf")

name = browser.find_element_by_id("project_name")
name.send_keys("PRIDA")






