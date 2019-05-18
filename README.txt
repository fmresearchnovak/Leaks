This project has two parts (1) the app itself, which collects location information and sends it to (2) the server which simply recieves and prints the information.  So, for the app to function fully the server must be running on some machine and the app must be pointed to that machine.

You can run the server code on any machine by running: 
./serv.py 
 - or - 
python3 serv.py  

The server will remaining running, printing any messages it recieves until you press crtl-c to kill it.  Before you run the server, you should figure out your own ip address so you can modify the settings in the app.  To see the ip address on a given linux machine, run
ip addr

You can see an example of this command on my machine,which is where I got the default ip address from, in ip_addr_screenshot.png

The app will work without this server running.  But, when it tries to send data out the data will go nowhere.  I recommend you run the server on your machine that you're developing on.  In the future we may run this on cs-41.fandm.edu which is a real server machine that the department has allocated for me to use for research.


