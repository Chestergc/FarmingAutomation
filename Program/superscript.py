##DOCUMENTATION:
###https://gpiozero.readthedocs.io/en/stable/api_input.html

##IMPORTS GO HERE
import os               #Gerenciamento de arquivos
import gpiozero         #GPIO nova, sensores
import bluetooth        #Bluetooth
import subprocess       #processamento paralelo, shell
import json             #output em json para servidor
import time             #time.sleep, time.time
import threading        #MultiThreading
import Adafruit_DHT     #DHT11 lib

##SETUP
###Input
waterSensor = gpiozero.Button(pin=22, pull_up=False)        ##WATER SENSOR AS BUTTON
ldr=gpiozero.LightSensor(pin=21)                            ##LDR as LightSensor
sensor=Adafruit_DHT.DHT11                                   ##DHT from Lib

###OUTPUT
waterValve = gpiozero.OutputDevice(pin=23, active_high=True, initial_value=False)
red=gpiozero.LED(4)                 ##RED MEANS STOP
green=gpiozero.LED(17)               ##GREEN MEANS GO
blue=gpiozero.LED(24)                ##BLUE MEANS CRY

###Variables
#mac="10:3B:59:B1:B3:C8"             #Endereço bluetooth android
day, soil, ctrLogs = "Escuro", "Seco", 1#LDR, SoilSensor, LogCounter
freq = 60                           #Read Frequency
lastdate, lasthour = "", ""         #Last Irrigation data

#CLASSES
''' This is for Threading, later on if time serves it's purpose
class bluetooth (threading.Thread):
    def __init__(self, threadID, name, counter):
       threading.Thread.__init__(self)
       self.threadID = threadID
       self.name = name
       self.counter = counter
    def run(self):
       print ("Starting " + self.name)
       comms(self.name)
       print ("Exiting " + self.name)
'''

##FUNCTIONS

def checkWater(wtrsens):
    if wtrsens.wait_for_press(timeout=1)==True:
        return True
    else:
        return False

def outputWater(checkwtr):
    toggle = checkwtr
    global lastdate
    global lasthour
    #print("checking water")
    if toggle==True:
        ##Toggle Output;
        waterValve.on()
        blue.on()
        ##Time/date Server
        timebufi=str(time.strftime('%X %x'))
        actuali=timebuf.split()
        lastdate = str(actual[1])
        lasthour = str(actual[0])
        #Sleep+turnoff blueled
        time.sleep(1)
        waterValve.off()
        blue.off()
        return True#(%s = y)
    else:
        return False

def checkLight(chklight):
    #print("checking light")
    if chklight.light_detected==True:
        return True
    else:
        return False

def endOfCycle(x):
    red.on()
    time.sleep(x)
    red.off()
    return

##JSON Server
def convert_bytes(num):
    for x in ['bytes', 'KB', 'MB', 'GB', 'TB']:
        if num < 1024.0:
            return "%3.1f %s" % (num, x)
        num /= 1024.0


def file_size(file_path):
    if os.path.isfile(file_path):
        file_info = os.stat(file_path)
        return convert_bytes(file_info.st_size)

def server(logFrequency, numberOfLogs, logs, lastdate, lasthour):
    print("Updating Server")
    ##JSON OUTPUT
    bufdict = outputDict
    with open("server.json", 'w') as fp:
        json.dump(bufdict, fp)
        fp.close()
    with open("backlog.json", 'a') as wp:
        json.dump(bufdict, wp)
        wp.close()

##FEEDBACK LOOP
if __name__ == "__main__":
    print("Starting main")
    while True:
        #WorkingLED==ON
        green.on()
        #fixTime variables
        timebuf=str(time.strftime('%X %x %A %p'))
        actual=timebuf.split()
        hour=str(actual[0])
        hour=hour[:-3]+str(actual[3])
        date=str(actual[1])
        weekd=str(actual[2])
        #print("DHT try 1")
        humidity, temperature=Adafruit_DHT.read_retry(sensor, 18)
        #print("DHT try 2")
        humidity, temperature=Adafruit_DHT.read_retry(sensor, 18)

        ##FixVars

        if temperature is None:
            #print("no temperature")
            temperature = "N/A"
        else:
            temperature = int(temperature)

        if humidity is None:
            #print("no humidity")
            humidity = "N/A"
        else:
            humidity = int(humidity)

        if(checkLight(ldr)==True):
            day="Claro"
        else:
            day="Escuro"

        if(checkWater(waterSensor)==True):
            soil="Úmido"
        else:
            soil="Seco"
        #print("sensor read over")
        ##InputFile
        client=open('client.json')
        client=client.read()
        inDict=dict(json.loads(client))
        client.close()
        ##OutputWater
        if inDict['auto'] = 'True':
            outputWater(checkWater(waterSensor))
        elif inDict['auto'] = 'False':
            nots = inDict['numberOfTriggers']
            while nots>-1:
                nots-=1
                if inDict['triggers'][nots]['oneTime']=='True' and date==inDict['triggers'][nots]['startDate'] and hour==inDict['triggers'][nots]['startTime']:
                    outputWater(checkWater(waterSensor))
                elif inDict['triggers'][nots]['oneTime']=='False' and date==inDict['triggers'][nots]['startDate'] and weekd in inDict['triggers'][nots]['daysOfTheWeek']:
                    outputWater(checkWater(waterSensor))
                else:
                    print('Not scheduled')

        ##OutputFix
        outputDict = {
        'logFrequency':freq,
        'numberOfLogs':ctrLogs,
        'lastdate':lastdate,
        'lasthour':lasthour,
        'logs':[{
            'date':date,
            'time':hour,
            'temperature':temperature,
            'moisture':humidity,
            'luminosity':day,
            'soil':soil
            }]
        }

        #Processing
        server(**outputDict)
        ctrLogs+=1
        green.off()
        endOfCycle(outputDict['logFrequency'])


        ##End BluetoothSocket
        ##client_socket.close()
        ##server_socket.close()
