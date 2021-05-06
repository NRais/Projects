
from collections import defaultdict

import requests
from bs4 import BeautifulSoup
import csv

URL = 'https://thegoodregistry.com/collections/all-charities'
web = requests.get(URL)

content = BeautifulSoup(web.content, 'html.parser')

def main():
    print("Summer of Tech Python Bootcamp")
    print(web)
    
    title_desc = defaultdict()
    items = content.find_all("div", class_="centered");
    
    # ------ FOR ALL ITEMS ------
    
    for i in items:
        # ensure i is the correct format
        encoded_string = i.encode('utf-8').decode('utf-8')
        
        # replace all html tags and white spaces from the text
        # PERSONALIZED parsing of text
        clean_string = encoded_string.replace('<div class="centered">', '')\
                                      .replace('<div style="padding-top: 10px;">', '')\
                                      .replace('</div>', '')\
                                      .replace('Click here to find out more.', '')\
                                      .replace('-', '')\
                                      .strip()
                               
        # break i into pieces title, description                       
        unfiltered_list = clean_string.split('\n')
        
        # setup the item
        title_desc[unfiltered_list[0]] = unfiltered_list[2].strip() if len(unfiltered_list) == 3 else ''
        
    write_csv(title_desc)
    
    
def write_csv(input: dict):
    # create and write to a file
    with open('result.csv', 'w', newline='') as f:
        writer = csv.writer(f)
        # get the data from the table we created and write rows
        for title, desc in input.items():
            writer.writerow([title, desc])
        
    
    
if __name__ == '__main__':
    main()
    
    
# potential object design for more complicated classes
class User():
    name: str
    address: str
    
class Child(User):
    child_name: str
    
