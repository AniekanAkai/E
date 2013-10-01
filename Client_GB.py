# Python GB client
# Wiring scheme:
# GP25 in J2 to B1 in J3
# GP24 in J2 to B2 in J3
# GP23 in J2 to B3 in J3
# GP11 in J2 to B5 in J3
# GP10 in J2 to B6 in J3
# GP9 in J2 to B7 in J3
# GP8 in J2 to B8 in J3
# GP18 in J2 to B11 in J3
# GP17 in J2 to B12 in J3
# jumpers on U4-out-B5 ... U4-out-B8
# jumpers on U5-out-B11 ... U4-out-B12


import RPi.GPIO as GPIO
import socket
from time import sleep

GPIO.setmode(GPIO.BCM) # initialise RPi.GPIO
GPIO.setwarnings(False)
for i in range(23,26): # set up ports 23-25
	GPIO.setup(i, GPIO.IN, pull_up_down=GPIO.PUD_UP) # as inputs pull-ups high
for i in range(7,12):	# set up ports 8-11
	GPIO.setup(i, GPIO.OUT)	# as LED outputs
for i in range(17,19):	# set up ports 0-1
	GPIO.setup(i, GPIO.OUT, initial=GPIO.HIGH)	# as LED indicators


# port aliases
but_tog = 25
but_cyc = 24
but_snd = 23
led1 = 11
led2 = 10
led3 = 9
led4 = 8
ledflash = [18,17]

#initialize values
led_curr = led1
r = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
r.connect(("10.0.0.1",4444))

def cyc_callback(but_cyc): #cycles to next led when cycle button is pressed
	global led_curr, led1, led4

	if led_curr == led4:
		led_curr = led1
	else:
		led_curr -= 1
	
def tog_callback(but_tog): #toggles current led when toggle button is pressed
	global led_curr, led1, led4

	GPIO.output(led_curr, not GPIO.input(led_curr))

def snd_callback(but_snd): #sends led status to server
	global led_curr, led1, led2, led3, led4

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect(("10.0.0.1", 4444))
	led_status = [GPIO.input(led1), GPIO.input(led2), GPIO.input(led3), GPIO.input(led4)] #update status
	led_str =''.join(map(str,led_status))	
	s.send(led_str)
	print led_status
	s.close()

def flash(): #blinks end leds 3 times on correct guess
	for i in (0,6):
		sleep(0.25)
		for num in ledflash:
			GPIO.output(num, not GPIO.input(num))
	for num in ledflash:
		GPIO.output(num, 1)
		
	
#detect button presses
GPIO.add_event_detect(but_tog, GPIO.FALLING, callback=tog_callback, bouncetime=200)
GPIO.add_event_detect(but_cyc, GPIO.FALLING, callback=cyc_callback, bouncetime=200)
GPIO.add_event_detect(but_snd, GPIO.FALLING, callback=snd_callback, bouncetime=200)

flash()

try:
	while True:
		sleep(0.11)
		if (r.recv(1024)=="win"):
			flash()
		
	
except KeyboardInterrupt:
	GPIO.cleanup()
GPIO.cleanup()


	
