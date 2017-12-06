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
ldr=gpiozero.LightSensor(pin=18)                            ##LDR as LightSensor
sensor=Adafruit_DHT.DHT11                                   ##DHT from Lib

###OUTPUT
waterValve = gpiozero.OutputDevice(pin=23, active_high=True, initial_value=False)
red=gpiozero.LED(4)                 ##RED MEANS STOP
green=gpiozero.LED(17)               ##GREEN MEANS GO
blue=gpiozero.LED(24)                ##BLUE MEANS CRY

###Variables
mac="10:3B:59:B1:B3:C8"             #Endereço bluetooth android
day, soil, ctrLogs = "day", "low", 1#LDR, SoilSensor, LogCounter
freq, senscount = 60, 4             #Read Frequency, Number of Sensors


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
    if wtrsens.wait_for_press()==True:
        return True
    else:
        return False

def outputWater(checkwtr):
    toggle = checkwtr
    if toggle==True:
        ##Toggle Output;
        waterValve.on()
        blue.on()
        time.sleep(1)
        waterValve.off()
        blue.off()
        return True#(%s = y)
    else:
        return False

def checkLight(chklight):
    if chklight.wait_for_light()==True:
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

#def comms(threadName):
    #print("Starting", threadName)
    #server_socket=bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    #port=1
    #server_socket.bind(("",port))
    #server_socket.listen(1)
    #client_socket, address = server_socket.accept()
    #print("Conexão Estabelecida em", address)

def server(logFrequency, numberOfLogs, logs):
    print("Updating Server")
    ##JSON OUTPUT
    bufdict = outputDict
    with open("server.json", 'w') as fp:
        json.dump(bufdict, fp)
        fp.close()
    with open("backlog.json", 'a') as wp:
        json.dump(bufdict, wp)
        wp.write("\n")
        wp.close()
    #ServerFixSize
    if(int(file_size(server.json).split()[0])>=10 and file_size(server.json).split()[1]=="MB"):
        subprocess.call(['cp server.json'+'bkp'])
        return
    else:
        return

##FIX BLUETOOTH DATA INPUT:
def fix(x):
    msg = msg[2:-1]
    inputdict = json.loads(msg)
    return inputdict


##FEEDBACK LOOP
if __name__ == "__main__":
    print("Starting main")
    while True:
        #WorkingLED==ON
        green.on()
        print("green")

        ##FOR LATER IF THERE IS TIME
        #ThreadSetup
        #bltThread = bluetooth(1, "Comms-1", 1)
        #bltThread.start()

        ##OutputFix
        #try:
        #    data=client_socket.recv(1024)
        #    data=fix(data)
        #except :
        #    pass
        timebuf=str(time.strftime('%X %x'))
        actual=timebuf.split()
        print(actual)
        date=str(actual[1])
        hour=str(actual[0])
        humidity, temperature=Adafruit_DHT.read_retry(sensor, 18)
        humidity, temperature=Adafruit_DHT.read_retry(sensor, 18)

        if(checkLight(ldr)==True):
            day="day"
        else:
            day="night"

        print(day)

        if(checkWater(waterSensor)==True):
            soil="High"
        else:
            soil="Low"

        print(soil)

        outputDict = {
        'logFrequency':freq,
        'numberOfLogs':ctrLogs,
        'logs':[{
            'date':date,
            'time':hour,
            'numberOfSensors':senscount,
            'temperature':temperature,
            'moisture':humidity,
            'luminosity':day,
            'soil':soil
            }]
        }

        #Processing and Output
        outputWater(checkWater(waterSensor))
        print(outputDict)
        #server(mac)
        ctrLogs+=1
        green.off()
        print("green")
        endOfCycle(outputDict['logFrequency'])


        ##End BluetoothSocket
        ##client_socket.close()
        ##server_socket.close()
