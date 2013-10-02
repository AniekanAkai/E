import sys
import socket
import pifacecommon.core
import pifacecommon.interrupts
import pifacedigitalio
import os
import time

#create socket 
r = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
r.connect(('10.0.0.1', 4444))
current_led = 0
pfd=pifacedigitalio.PiFaceDigital()

#this function toggles the state of the currently selected LED
def toggle(event):
        global current_led
        pfd.leds[current_led].toggle()

#this function will cycle the currently selected LED to the next one
#loops back to the first LED if it reaches the 4th
def next(event):
        global current_led
        if current_led < 3:
                current_led += 1
        else:
                current_led = 0

#sends the current LED configuration to the server
def send(event):
        global config,r
#       s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#       s.connect(('10.0.0.1',4444))
        config = [pfd.leds[0].value,pfd.leds[1].value,pfd.leds[2].value, pfd.leds[3].value]
        led_status = ''.join(map(str,config))
        r.send(led_status)

#flashes the LEDs and exits the program
def flash():
        pfd.output_port.all_on()
        time.sleep(0.5)
        pfd.output_port.all_off()
        time.sleep(0.5)
        pfd.output_port.all_on()
        time.sleep(0.5)
        pfd.output_port.all_off()
        r.close()
        listener.deactivate()
        sys.exit(1)

#initiate the modules used, .core might not be used I dont remember
pifacecommon.core.init()
pifacedigitalio.init()
#create the event listener(for the interrupts)
listener = pifacedigitalio.InputEventListener()

#add the listener to the buttons and assign the appropriate callbacks
#to the appropriate buttons 
#button 0 toggles LED status
#button 1 goes to next LED
#button 2 sends current configuration of LEDs
listener.register(0, pifacecommon.interrupts.IODIR_ON, toggle)
listener.register(1, pifacecommon.interrupts.IODIR_ON, next)
listener.register(2, pifacecommon.interrupts.IODIR_ON, send)

#start listening for interrupts
listener.activate()

#infinite loop so program doesnt end
#constantly listens for server to inform if sent config is correct
#if it recieves the 'win' string from the server it calls flash which
#terminates the program
while True:
        time.sleep(1)
        if r.recv(1024)=='win':
                flash()


