# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import json
import requests
import psycopg2
from datetime import datetime
from lxml import html, etree
from selenium import webdriver
driver = webdriver.PhantomJS()
zh_driver = webdriver.PhantomJS()


try:
    # For Python 3.0 and later
    from urllib.request import urlopen
except ImportError:
    # Fall back to Python 2's urllib2
    from urllib2 import urlopen


class Race:
    def __init__(self, date, raceCourt, raceNo):
        self.date = date
        if (raceCourt == 'ST'):
            self.isShatin = 1
            self.isHappyValley = 0
        else :
            self.isShatin = 0
            self.isHappyValley = 1
        self.raceNo = raceNo

class RaceDividend:
    def __init__(self, pool, order, combination, ratio):
        self.pool = pool
        self.order = order
        self.combination = combination
        self.ratio = ratio

class Horse:
    def __init__(self, name, plc, draw, jockey, trainer, ratio):
        self.name = name
        self.plc = plc #名次
        self.draw = draw #檔位
        self.jockey = jockey #騎師
        self.trainer = trainer #練馬師
        self.ratio = ratio #獨羸

#params = (
#    ('lang', 'ch'),
#    ('date', '2020-03-10'),
#    ('venue', 'ST'),
#    ('raceno', '1'),
#)

#response = requests.get('https://bet.hkjc.com/racing/index.aspx', params=params)

#print(response.text)
        
        
def getRemoteUri(url) :
    try : 
        testLink = requests.get(url)
        print(testLink.status_code)
    except Exception as e: 
        print(str(e))
        return -999
    
    if (testLink.status_code != 200):
        return -999
    
    return 1


MAX_RETRY_COUNT=5

for raceCounter in range (1, 15): #from 1 to 14 (15)
    raceDate = '2020/03/22'
    raceCourt = 'ST'
    raceNo = raceCounter
    raceContainer = Race(raceDate, raceCourt, raceNo)
    
    url = 'https://racing.hkjc.com/racing/information/English/Racing/LocalResults.aspx?RaceDate=' + raceDate + '&Racecourse=' + raceCourt + '&RaceNo='  + str(raceNo)
    zh_url = 'https://racing.hkjc.com/racing/information/Chinese/Racing/LocalResults.aspx?RaceDate=' + raceDate + '&Racecourse=' + raceCourt + '&RaceNo='  + str(raceNo)
    #url="file:///C:/Users/Ming/Desktop/HKJC/race_eng.html"
    
    print('============================================================')
    print('Retriving from URL : ' + url)
    #tree = page.content.decode()
    
    #content = etree.HTML(response.text)
    
    #test = content.xpath('//*[@id="raceDiv1"]/text()')
    
    #print(test)
    
    if (getRemoteUri(url) == -999 | getRemoteUri(zh_url) == -999):
        continue
    
    
    #race_element = driver.find_element_by_id(id_='raceDiv1')
    #print(race_element.text)
    
    
    
    #horse_element = driver.find_element_by_id(id_='raceResDiv')
    #print(horse_element.text)
    
    #result_element = driver.find_element_by_class_name('dividend_tab')
    #print(result_element.text)
    
    errorFlag=0
    
    for retryCounter in range(1, MAX_RETRY_COUNT + 1):
        print('Attempt : ' + str(retryCounter))
        driver.get(url)
        zh_driver.get(zh_url)
    
        try: 
            table = driver.find_element_by_class_name('dividend_tab')
            tableRank = driver.find_element_by_class_name('performance')
            zh_table = zh_driver.find_element_by_class_name('dividend_tab')
            zh_tableRank = zh_driver.find_element_by_class_name('performance')
            #print(table.text)
            print('----')
            break
        except Exception as e: 
            if (retryCounter == MAX_RETRY_COUNT) :
                errorFlag=1
            
    if (errorFlag == 1):
        continue
    
    #=============================
    #Get Horse Rank
    #=============================
    horseRank=[]
    horseRank.append((tableRank.find_elements_by_css_selector('tr')[1]).find_elements_by_css_selector('td')[1].text)
    horseRank.append((tableRank.find_elements_by_css_selector('tr')[2]).find_elements_by_css_selector('td')[1].text)
    horseRank.append((tableRank.find_elements_by_css_selector('tr')[3]).find_elements_by_css_selector('td')[1].text)
    horseRank.append((tableRank.find_elements_by_css_selector('tr')[4]).find_elements_by_css_selector('td')[1].text)

    jsonHorseRankStr = json.dumps(horseRank)
    print(jsonHorseRankStr)
    
    #=============================
    #Get Rank 1-4 Horse details
    #=============================
    horseInfo=[]
    for horseInfoCount in range(1,5):
        h_name = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[2].text
        h_plc = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[0].text 
        h_draw = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[7].text 
        h_jockey = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[3].text 
        h_trainer = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[4].text
        h_ratio = (zh_tableRank.find_elements_by_css_selector('tr')[horseInfoCount]).find_elements_by_css_selector('td')[11].text
        horse=Horse(h_name, h_plc, h_draw, h_jockey, h_trainer, h_ratio)
        horseInfo.append(horse)
    
    jsonHorseInfoStr = json.dumps([d.__dict__ for d in horseInfo],ensure_ascii=False)
    print(jsonHorseInfoStr)
        
    #=============================
    #Get dividend
    #=============================
    listOfTags = ['WIN', #獨贏
                  'PLACE', #位置 
                  'QUINELLA',  #連贏
                  'QUINELLA PLACE',  #位置Q
                  '3 PICK 1', '3 PICK 1\n(COMPOSITE WIN)',  #3揀1（組合獨贏）
                  'TIERCE',  #三重彩
                  'TRIO', #單T
                  'FIRST 4',  #四連環
                  'QUARTET', #四重彩
                  '1ST DOUBLE', 
                  '2ND DOUBLE', 
                  '3RD DOUBLE', #第三口孖寶	
                  '4TH DOUBLE',
                  '5TH DOUBLE', #第五口孖寶
                  '6TH DOUBLE', #第六口孖寶	
                  '7TH DOUBLE', #第七口孖寶
                  '8TH DOUBLE', #第八口孖寶	
                  '9TH DOUBLE', #第九口孖寶	
                  '10TH DOUBLE', 
                  '11TH DOUBLE', 
                  '12TH DOUBLE', 
                  '13RD DOUBLE', 
                  '14TH DOUBLE', 
                  '1ST DOUBLE TRIO', #第一口孖T
                  '2ND DOUBLE TRIO', #第二口孖T	
                  '3RD DOUBLE TRIO', #第三口孖T	
                  'TREBLE', #三寶	
                  'TRIPLE TRIO', #三T
                  'TRIPLE TRIO(Consolation)', #三T(安慰獎)
                  'SIX UP', #六環彩	
                  ]
                  
    raceHist=[]
    lastTag = ''
    lastTagCount = 0
                                    
    for row in table.find_elements_by_css_selector('tr'):
        #print([d.text for d in row.find_elements_by_css_selector('td')])
        tdList = row.find_elements_by_css_selector('td')
    
        
        #print("DEBUG: " + lastTag + str(lastTagCount))
        
        if (tdList[0].text in listOfTags):
            lastTag = tdList[0].text.replace('\n(COMPOSITE WIN)', '')
            lastTagCount = 0
            
            try: 
                winType=RaceDividend(lastTag, lastTagCount, tdList[1].text, float(tdList[2].text.replace(',', '')))
            except:
                winType=RaceDividend(lastTag, lastTagCount, tdList[1].text, -1)
            
            #print("DEBUG: (in list)" + lastTag + str(lastTagCount))
            #
            #winType=[]
            #winType.append(tdList[0].text)
            #winType.append(tdList[1].text)
            #try: 
            #    winType.append(float(tdList[2].text.replace(',', '')))
            #except:
            #    winType.append(-1)
            raceHist.append(winType)
        elif (lastTag in listOfTags):
            winType=[]
            lastTagCount += 1
            
            try: 
                winType=RaceDividend(lastTag, lastTagCount, tdList[0].text, float(tdList[1].text.replace(',', '')))
            except:
                winType=RaceDividend(lastTag, lastTagCount, tdList[0].text, -1)
            
            #print("DEBUG: (not in list)" + lastTag + str(lastTagCount))
            
            #winType.append(lastTag + str(lastTagCount))
            #winType.append(tdList[0].text)
            #try: 
            #    winType.append(float(tdList[1].text.replace(',', '')))
            #except:
            #    winType.append(-1)
            raceHist.append(winType)
        
        #for i in range(len(tdList)):
        #    print(tdList[i].text)
        
    jsonDividendStr = json.dumps([d.__dict__ for d in raceHist])
    print(jsonDividendStr)
    
    jsonRaceStr = json.dumps(raceContainer.__dict__)
    print(jsonRaceStr)
    
    timestamp = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    
    conn = psycopg2.connect(database="jockey", user="postgres", password="sa", host="127.0.0.1", port="5432")
    print('Opened database successfully')
    cursor = conn.cursor()
    
    selectQuery='select count(1) from race_hist where race_date = %s and race_no = %s' 
    selectTuple = [raceDate, raceNo]
    cursor.execute(selectQuery, selectTuple)
    (selectCount,)=cursor.fetchone()
    #print(selectCount)

    if (selectCount == 0):
        insertQuery='insert into race_hist (race_date, race_no, create_dt, update_dt, horse_rank, horse_info, race_details, race_dividend) values (%s, %s, %s, %s, %s, %s, %s, %s)'
        insertTuple = [raceDate, raceNo, timestamp, timestamp, jsonHorseRankStr, jsonHorseInfoStr, jsonRaceStr, jsonDividendStr]
        cursor.execute(insertQuery, insertTuple)
        conn.commit()
        print('Insert record successfully : ' + raceDate + ' ' + str(raceNo))
    else :
        updateQuery='update race_hist set horse_rank=%s, update_dt=%s, race_details=%s, horse_info=%s, race_dividend=%s where race_date=%s and race_no=%s'
        updateTuple=[jsonHorseRankStr, timestamp, jsonRaceStr, jsonHorseInfoStr, jsonDividendStr, raceDate, raceNo]
        cursor.execute(updateQuery, updateTuple)
        conn.commit()
        print('Update record successfully : ' + raceDate + ' ' + str(raceNo))
        
    conn.close()
    
    
    