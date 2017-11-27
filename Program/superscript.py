##DOCUMENTATION:
###https://gpiozero.readthedocs.io/en/stable/api_input.html

##IMPORTS GO HERE
import RPi.GPIO as GPIO #GPIO antiga, setup do sistema
import gpiozero         #GPIO nova, sensores
import Bluetooth        #Bluetooth
import subprocess       #processamento paralelo, shell
import json             #output em json para servidor
import time             #sleep, time.time
import threading

##SETUP
GPIO.setmode(GPIO.BCM)  #Setup de nome de pinos
GPIO.setwarnings(False) #desabilita verbosidade de argumentos
GPIO.setup(pin,GPIO.OUT)#Setup pino como saida
GPIO.output(pin,0)      #Reseta pino de saida
waterSensor = gpiozero.InputDevice(pin, pull_up=False)
waterValve = gpiozero.OutputDevice(pin, active_high=True, initial_value=False)
shutdown_btn = Button(17, hold_time=2)#Desliga
shutdown_btn.when_held = shutdown
mac="macAdress"         #Endereço bluetooth android
#file.new(server.json)  #Cria documento de servidor

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

class server (threading.Thread):
    def __init__(self, threadID, name, counter):
       threading.Thread.__init__(self)
       self.threadID = threadID
       self.name = name
       self.counter = counter
    def run(self):
       print ("Starting " + self.name)
       server(self.name)
       print ("Exiting " + self.name)

##FUNCTIONS

def startupbtl(threadName):
    print("Starting", threadName)
    subprocess.call(['sudo', 'bluetoothctl'])
    subprocess.call(['power', 'on'])
    subprocess.call(['discoverable', 'on'])
    subprocess.call(['pairable', 'on'])
    subprocess.call(['scan', 'on'])
    subprocess.call(['pair <', mac, '>'])

def comms(threadName):
    print("Starting", threadName)
    server_socket=bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    port=1
    server_socket.bind(("",port))
    server_socket.listen(1)
    client_socket, address = server_socket.accept()
    print("Conexão Estabelecida em", address)

def checkWater(x,thresh):
    needsWater = x
    if needsWater>=thresh:
        return True
        sleep(1)
    else:
        return False

def outputWater(x,y,z):
    Toggle = x
    if x==True:
        ##Toggle Output;
        waterValve.on()
        sleep(1)
        waterValve.off()
        ##ServerData;
        return "The water valve was turned on %S"#(%s = y)
        sleep(1)
    else:
        return "The water valve is off"

def endOfCycle(x):
    sleep(x)
    return 0

def shutdown():
    check_call(['sudo', 'poweroff'])

##JSON Server
def server(threadName,**outputDict):
    print("Starting", threadName)
    ##JSON OUTPUT
    file.open(server.json)
    file.append(json.dumps(outputDict))
    file.close(server.json)

def fix(x):
    return



##FEEDBACK LOOP
if __name__='__main__':
    while True:
        #ThreadSetup
        bltThread = bluetooth(1, "Comms-1", 1)
        srvThread = server(2, "Server-1", 2)
        bltThread.start()

        ##OutputFix
        outputDict = {
        'logFrequency':60,
        'numberOfLogs':1,
        'logs':[{
            'date':'12/11/2017',
            'time':'06:11:00',
            'numberOfSensors':4,
            'sensors':
                [{"name":"Temperature",
                "data":'22.3',
                "unit":"°C"},
                {"name":"Moisture",
                "data":35,
                "unit":"%"},
                {"name":"Luminosity",
                "data":11000,
                "unit":"Lux"},
                {"name":"Soil Moisture",
                "data":"Low",
                "unit":""}]
                }]
        }

        #Processing and Output
        srvThread.start()
        outputWater(checkWater(pin), time.time, pin)
        data=client_socket.recv(1024)
        fix(data)
        endOfCycle(10)
        ##End BluetoothSocket
        ##client_socket.close()
        ##server_socket.close()
