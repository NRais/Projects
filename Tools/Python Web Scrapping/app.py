
from collections import defaultdict

import requests
from bs4 import BeautifulSoup

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
        encoded_string = i.encode('utf-8').decode('utf-8')
        
        clean_string = encoded_string.replace('<div class="centered">', '')\
                                      .replace('<div style="padding-top: 10px;">', '')\
                                      .replace('</div>', '')\
                                      .replace('Click here to find out more.', '')\
                                      .replace('-', '')\
                                      .strip()
                               
        unfiltered_list = clean_string.split('\n')
        
        title_desc[unfiltered_list[0]] = "Summer of Tech"
        
        print(unfiltered_list)
        
    
    
if __name__ == '__main__':
    main()