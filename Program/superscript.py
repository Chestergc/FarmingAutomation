##DOCUMENTATION:
###https://gpiozero.readthedocs.io/en/stable/api_input.html

##IMPORTS GO HERE
import os               #Gerenciamento de arquivos
import gpiozero         #GPIO nova, sensores
import Bluetooth        #Bluetooth
import subprocess       #processamento paralelo, shell
import json             #output em json para servidor
import time             #time.sleep, time.time
import threading        #MultiThreading
import Adafruit_DHT

##SETUP
###Input
waterSensor = gpiozero.Button(pin=18, pull_up=false)
ldr=gpiozero.LightSensor(pin=15)
sensor=Adafruit_DHT.DHT11
humidity, temperature=Adafruit_DHT.read_retry(sensor, 14)

###OUTPUT
waterValve = gpiozero.OutputDevice(pin=23, active_high=True, initial_value=False)
red=gpiozero.LED(1)
green=gpiozero.LED(3)
blue=gpiozero.LED(5)

###Variables
mac="macAdress"         #Endereço bluetooth android
day, soil, ctrLogs, freq, senscount = "day", "low", 1, 60, 4


#CLASSES

class bluetooth (threading.Thread):
    def __init__(self, threadID, name, counter):
       threading.Thread.__init__(self)
       self.threadID = threadID
       self.name = name
       self.counter = counter
    def run(self):
       print ("Starting " + self.name)
       startupbtl(self.name)
       comms(self.name)
       print ("Exiting " + self.name)

##FUNCTIONS

def startupbtl(threadName):
    print("Starting", threadName)
    subprocess.call(['sudo', 'bluetoothctl'])
    subprocess.call(['power', 'on'])
    subprocess.call(['discoverable', 'on'])
    subprocess.call(['pairable', 'on'])
    subprocess.call(['scan', 'on'])
    subprocess.call(['pair', mac])

def comms(threadName):
    print("Starting", threadName)
    server_socket=bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    port=1
    server_socket.bind(("",port))
    server_socket.listen(1)
    client_socket, address = server_socket.accept()
    print("Conexão Estabelecida em", address)

def checkWater(wtrsens):
    wtrsens = x
    if x.wait_for_press()==True:
        return True
    else:
        return False

def outputWater(checkwtr):
    Toggle = checkwtr
    if x==True:
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
    if chklight.waitforlight()==True:
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


def server(mac, **outputDict):
    print("Updating Server")
    ##JSON OUTPUT
    srvFile = os.open("server.json", os.O_APPEND|os.O_CREAT)
    os.write(srvFile, json.dumps(outputDict))
    os.close(srvFile)

    #bluetooth
    bd_addr=mac
    port=1

    sock=bluetooth.BluetoothSocket( bluetooth.RFCOMM )
    sock.connect((bd_addr, port))

    sock.send(outputDict['logs']['date'])
    sock.send(outputDict['logs']['time'])
    sock.send(outputDict['logs']['numberOfSensors'])
    sock.send(outputDict['logs']['temperature'])
    sock.send(outputDict['logs']['moisture'])
    sock.send(outputDict['logs']['luminosity'])
    sock.send(outputDict['logs']['soil'])
    sock.close()

    #ServerFixSize
    if(int(file_size(server.json).split()[0])>=10 and file_size(server.json).split()[1]=="MB"):
        subprocess.call(['cp server.json'+'bkp'])
        return
    else:
        return


##FIX BLUETOOTH DATA INPUT:
#def fix(str(x)):
#    fix=x.split()
#
#    return


##FEEDBACK LOOP
def main():
    while True:
        #WorkingLED==ON
        green.on()

        #ThreadSetup
        bltThread = bluetooth(1, "Comms-1", 1)
        bltThread.start()

        ##OutputFix
        data=client_socket.recv(1024)
        data=fix(data)
        timebuf=str(time.strftime('%X %x'))
        actual=timebuf.split()
        day=str(actual[0])
        hour=str(actual[1])
        humidity, temperature=Adafruit_DHT.read_retry(sensor, 14)

        if(checkLight(ldr)==True):
            day="day"
        else:
            day="night"

        if(checkWater(WaterSensor)==True):
            soil="High"
        else:
            soil="Low"

        outputDict = {
        'logFrequency':freq,
        'numberOfLogs':ctrLogs,
        'logs':[{
            'date':day,
            'time':hour,
            'numberOfSensors':senscount,
            'temperature':temperature,
            'moisture':humidity,
            'luminosity':day,
            'soil':soil
            }]
        }

        #Processing and Output
        outputWater(checkWater(WaterSensor))
        server(mac)
        ctrLogs+=1
        green.off()
        endOfCycle(outputDict['logFrequency'])

        ##End BluetoothSocket
        ##client_socket.close()
        ##server_socket.close()


if __name__=='__main__':
    main()
