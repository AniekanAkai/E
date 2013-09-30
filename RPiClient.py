import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("localhost", 4444))
print s.recv(1024)
s.send("Hi Java server")
print "Message sent"
