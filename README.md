E
=

GitHub for Group E's trial project in SYSC 3010.

Set up the Gertboard with the following connections
# Wiring scheme:
GP25 in J2 to B1 in J3 \n
GP24 in J2 to B2 in J3 \n
GP23 in J2 to B3 in J3 \n
GP11 in J2 to B5 in J3 \n
GP10 in J2 to B6 in J3 \n
GP9 in J2 to B7 in J3 \n
GP8 in J2 to B8 in J3 \n
GP18 in J2 to B11 in J3 \n
GP17 in J2 to B12 in J3 \n
jumpers on U4-out-B5 ... U4-out-B8 \n
jumpers on U5-out-B11 ... U4-out-B12 \n
 
#Launch Instructions
Ensure that the server machine and both Pi’s are connected to the same router/switch.
Launch the RPiServer.Java program on the server
Launch PiFaceClient.py on the Raspberry Pi with the PiFace attachment
Launch Client_GB.py on the Raspberry Pi with the Gertboard attachment
