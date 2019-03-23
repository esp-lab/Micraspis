/*
Source code for
*/
#include <SoftwareSerial.h>
/*
Each state in the application corresponds to one the integer.
Numbered starting at 0
stateCurrent is a variable that stores the current state of the application.
Start: 0
Show: 1
*/
int currentState = 0;
int nextState = -1;
/*--------------------Define--------------------*/
/*Define Wifi ESP8266 - wifi */
#define TIMEOUT			3000
#define RX 10 // pin TX of ESP8266 connect to pin 10 of Arduino
#define TX 11 //pin RX of ESP8266 connect to pin 11 of Arduino
SoftwareSerial wifi(RX,TX);
/*Define LED - led output*/
const int led = 12;
/*--------------------END Define--------------------*/
/*--------------------Prototype--------------------*/
//Read response from esp. Arduino is a receiver
String readResponse();
//Arduino send command to the esp
String sendCommand(String command);
//Close connection with a link id
void closeConnect();
//Check the link ID
//if return  0,1,2,3,4 is connected.
//else is 5 and other is not connected
int checkConnect();
void sendResponse(String content);
void HandleRequest(String res);
void stateStart();
void stateShow();
/*--------------------END Prototype--------------------*/
void setup()
{
	Serial.begin(9600); //Change baud rate according to your ESP
	wifi.begin(9600);
	Serial.println("Starting...");
	Serial.println("Initializing module ...");
	Serial.println(sendCommand("AT+RST"));
	Serial.println(sendCommand("AT+CWMODE=3"));
	Serial.println(sendCommand("AT+CIFSR"));
	Serial.println("Configuring access point...");
	Serial.println(sendCommand("AT+CWSAP=\"ESPLAP\",\"1234567890\",5,3"));
	Serial.println(sendCommand("AT+CIPAP=\"192.168.4.22\",\"192.168.4.22\",\"255.255.255.0\""));
	Serial.println(sendCommand("AT+CIPMUX=1"));
	Serial.println(sendCommand("AT+CIPSERVER=1,80"));
	Serial.println(sendCommand("AT+CIFSR"));
	Serial.println("Server is ready.");
	pinMode(led, OUTPUT);
	if(currentState == 0){
		stateStart();
	}
}
void loop()
{
	//Listening the request from server
	String res = readResponse();
	if(res != ""){
		HandleRequest(res);
		String htmlResponse = "<!doctype html>"
    "<html>"
      "<head>"
        "<title>STDIO.VN ESP8266 DEMO</title>"
      "</head>"
      "<body>"
        "<h1>STDIO.VN ESP8266 DEMO</h1>"
        "<form action='' method='GET'>"
          "<input type='radio' name='LED' name='STDIO' value='STDIO_ON' /> LED ON<br/>"
          "<input type='radio' name='LED' name='STDIO' value='STDIO_OFF' /> LED OFF<br/>"
          "<input type='submit' value='Submit' />"
        "</form>"
      "</body>"
     "</html>";
	 sendResponse(htmlResponse);
	}
	delay(300);
	switch(currentState){
		case 0:
		//<case0>
		break;
		case 1:
		//<case1>
		break;
		default:
		break;
	}
}
/*--------------------Implement--------------------*/
String readResponse()
{
	String res = "";
	long timeout = millis() + TIMEOUT;
	while (millis() < timeout)
	{
		if (wifi.available())
		{
			char ch = wifi.read();
			res += ch;
		}
	}
	return res;
}
//Send command to esp.
String sendCommand(String command)
{
	wifi.println(command);
	return readResponse();
}
//Close connection with a link id
void closeConnect()
{
	Serial.println(sendCommand("AT+CIPSTATUS"));
	String res = readResponse();
	if (res.indexOf("+CIPSTATUS:0") >= 0)
	{
		Serial.println(sendCommand("AT+CIPCLOSE=0"));
	}
	if (res.indexOf("+CIPSTATUS:1") >= 0)
	{
		Serial.println(sendCommand("AT+CIPCLOSE=1"));
	}
	if (res.indexOf("+CIPSTATUS:2") >= 0)
	{
		Serial.println(sendCommand("AT+CIPCLOSE=2"));
	}
	if (res.indexOf("+CIPSTATUS:3") >= 0)
	{
		Serial.println(sendCommand("AT+CIPCLOSE=3"));
	}
	if (res.indexOf("+CIPSTATUS:4") >= 0)
	{
		Serial.println(sendCommand("AT+CIPCLOSE=4"));
	}
	Serial.println(sendCommand("AT+CIPCLOSE=5"));
}
//Check the link ID
//if return  0,1,2,3,4 is connected.
//else is 5 and other is not connected
int checkConnect()
{
	Serial.println(sendCommand("AT+CIPSTATUS"));
	String res = readResponse();
	if (res.indexOf("+CIPSTATUS:0") >= 0)
	{
		return 0;
	}
	else if (res.indexOf("+CIPSTATUS:1") >= 0)
	{
		return 1;
	}
	else if (res.indexOf("+CIPSTATUS:2") >= 0)
	{
		return 2;
	}
	else if (res.indexOf("+CIPSTATUS:3") >= 0)
	{
		return 3;
	}
	else if (res.indexOf("+CIPSTATUS:4") >= 0)
	{
		return 4;
	}
	else if (res.indexOf("+CIPSTATUS:5") >= 0)
	{
		return 5;
	}
	return -1;
}
void sendResponse(String content)
{
	String cmd = "AT+CIPSEND=0,";
	cmd += content.length();
	Serial.println(sendCommand(cmd));
	Serial.println(sendCommand(content));
	closeConnect();
}
void HandleRequest(String res){
	if (res.indexOf("GET / HTTP/1.1") >= 0)
	{
		String content = "Well come to ESP LAP";
		String cmd = "AT+CIPSEND=0,";
		cmd += content.length();
		Serial.println(sendCommand(cmd));
		Serial.println(sendCommand(content));
		closeConnect();
		res = "";
	}
	else if (res.indexOf("CONNECT") >= 0)
	{
		res = "";
		Serial.println("receive....");
	}
	else if (res.indexOf("+IPD") >= 0)
	{
		//Event: "turnOn" received
		if(res.indexOf("turnOn") != -1)
		{
			stateShow();
		}
		//Event: "turnOff" received
		if(res.indexOf("turnOff") != -1)
		{
			stateStart();
		}
		//<requestWifi>
		res = "";
	}
	closeConnect();
	delay(300);
}
void stateStart(){
	digitalWrite(led, LOW);
	currentState = 0;
	delay(200);
}
void stateShow(){
	digitalWrite(led, HIGH);
	currentState = 1;
	delay(200);
}
/*--------------------END Implement--------------------*/