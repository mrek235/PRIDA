from selenium import webdriver
from selenium.common.exceptions import TimeoutException

browser = webdriver.PhantomJS()
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


