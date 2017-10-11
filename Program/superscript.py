##DOCUMENTATION:
###https://gpiozero.readthedocs.io/en/stable/api_input.html

##IMPORTS GO HERE
import gpiozero
from time import sleep

##SETUP
waterSensor = gpiozero.InputDevice(pin, pull_up=False)
waterValve = gpiozero.OutputDevice(pin, active_high=True, initial_value=False)

##FUNCTIONS

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

def recordJSON(x):
    x=str(x)
    if str(x)==[::-1]:
        return "Already on file"
    else:
        file.open()
        file.append(x)
        file.close()
        return "Recorded"

def scrUpdateBuffer():
    file.open()##SERVER FILE
    sendToBuffer = file.read()##Last Input
    file.close()##SERVER FILE
    file.open()##SCREEN BUFFER FILE
    file.write(sendToBuffer)## CLEAN THE FILE BEFORE WRITING TO IT
    file.close()##SCREEN BUFFER

def scrRun():
    ##Run GUI SCRIPT
    file.open()
    uiStuff = file.read()##STUFF FROM THE BUFFER FILE
    exec(open("./GUI.py").read(), uiStuff)
    file.close()
    exec(close("./GUI.py"))

def endOfCycle(x):
    sleep(x)
    return 0

def shutdown():
    check_call(['sudo', 'poweroff'])

###CODE FOR SHUTDOWN BUTTON:
##shutdown_btn = Button(17, hold_time=2)
##shutdown_btn.when_held = shutdown


##SERVER SCRIPT

##JSON OUTPUT

##ACTUATING

##FEEDBACK LOOP
if __name__='__main__'
    scrRun()
    outputWater(checkWater(pin), time.time, pin)
    recordJSON(checkWater(pin))
    endOfCycle(10)
    ##THIS
