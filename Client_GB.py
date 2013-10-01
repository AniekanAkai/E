# Python GB client
# Wiring scheme:
# GP25 in J2 to B1 in J3
# GP24 in J2 to B2 in J3
# GP23 in J2 to B3 in J3
# GP10 in J2 to B5 in J3
# GP9 in J2 to B6 in J3
# GP8 in J2 to B7 in J3
# GP7 in J2 to B8 in J3
# jumpers on U4-out-B5 ... U4-out-B8

import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)                  # initialise RPi.GPIO
for i in range(23,26):                  # set up ports 23-25 
    GPIO.setup(i, GPIO.IN, pull_up_down=GPIO.PUD_UP)  # as inputs pull-ups high
for i in range(7,11):					# set up ports 7-10
	GPIO.setup(i, GPIO.OUT)				# as LED outputs
	
# port aliases
but_tog = 25
but_cyc = 24
but_snd = 23
led1 = 10
led2 = 9
led3 = 8
led4 = 7

#initialize values
led_curr = led1

#detect button presses
GPIO.add_event_detect(but_tog, GPIO.FALLING)
GPIO.add_event_detect(but_cyc, GPIO.FALLING)
GPIO.add_event_detect(but_snd, GPIO.FALLING)

try:
	while True:
		led_status = [GPIO.input(led1), GPIO.input(led2), GPIO.input(led3), GPIO.input(led4)] #update status
		if GPIO.event_detected(but_snd)
			print "Sent." #put UDP send led_status here
		elif GPIO.event_detected(but_cyc)
			if led_curr == led4 
				led_curr = led1
			else:
				led_curr -= 1
		elif GPIO.event_detected(but_tog)
			GPIO.output(led_curr, not GPIO.input(led_curr))
			
except KeyboardInterrupt:
	GPIO.cleanup()
GPIO.cleanup()

