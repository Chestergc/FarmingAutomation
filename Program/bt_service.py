
from bluetooth import *
import time
import os

base_dir = '/home/pi/Dripper/'
server_file = base_dir + 'server.json'
client_file = base_dir + 'client.json'

def read_data():
    try:
        f = open(server_file, 'r')
        lines = f.readlines()
        f.close()
    except IOError:
        print("Error reading file", server_file)
    return lines
    
def write_data(data):
    try:
        f = open(client_file, 'w')
        f.write(str(data))
        f.close()
    except IOError:
        print("Error writing file", client_file)

time.sleep(2)

#make device visible
#os.system("hciconfig hci0 piscan")

os.system("bluetoothctl")
time.sleep(0.5)
os.system("agent on")
time.sleep(0.5)
os.system("scan on")
time.sleep(0.5)

#Create socket and bind
server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "e821e169-d793-423b-927b-f9c7a5017fb1"

advertise_service( server_sock, "DripperServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )

while True:
    print ("Waiting for connection on RFCOMM channel %d" % port)
    client_sock = None
    client_sock, client_info = server_sock.accept()
    print ("Accepted connection from ", client_info)
        
    try:
        full_data = ""
        while True:
        
            data = client_sock.recv(1024)
            if len(data) == 0:
                client_sock.close()
                server_sock.close()
                break
        
            print ("received [%s]" % data)
            full_data+=str(data,"utf-8")
            if "#" in full_data:
                print("end of msg")
                end_char = full_data.find("#")
                write_data(full_data[:end_char])
                full_data = ""
                        
            data_to_send = read_data()
            data_len = len(data_to_send)
            if(data_len != 0):
                if(data_len < 1024):
                    data_to_send=data_to_send[:data_len]
                client_sock.send(str(data_to_send)+"#")
                print ("sending [%s]" % data_to_send)

    except IOError:
        pass

'''
except KeyboardInterrupt:
    print ("disconnected")
    if client_sock is not None:
        client_sock.close()
        
    server_sock.close()
    print ("all done")
    break
'''

print ("all done")
