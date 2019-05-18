#!/usr/bin/env python3

import socket
import sys
from threading import Thread


def handleConn(socket, addr):
	data = socket.recv(64)
	string = data.decode("utf-8")
	print("Message from " + str(addr[0]) + ": " + str(string))
	socket.close()



def main():

	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	server_address = ("", 10000)
	sock.bind(server_address)
	sock.listen(4096) # Number of unaccepted connections to allow
	
	print("Ready for connections!")
	while(True):
		#print("Waiting for a new connection")
		connection, client_addr = sock.accept()
		t = Thread(target = handleConn, args = (connection, client_addr))
		t.start()



main()
