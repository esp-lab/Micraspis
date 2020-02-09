# Micraspis

* [Overview][overview]
* [Command syntax][command-syntax]
* [Connection command syntax][connection-command-syntax]
* [Examples][examples]

## Overview

This is a tool allowing generate source code for wearable devices by describing devices configuration and state schema  http://esp-lab.net/

Gen Engine

![gen.png]

Mainboard Editor

![mainboard_editor_gui.png]

StatesSchema Editor

![stateschema_editor_gui.png]

## Command syntax

| No. |	Syntax | Description | Component | Example |
|:--|:--------------------------|:------------------------------------|:-----------------------------|:------------------------------|
|1	| `Display <id>`            |Activate a device.			              | LED, CDS		                 | `Display` ledRed              |
|2	| `Hidden <id>`	            |Order to stop working.	              | LED, CDS 		                 | `Hidden` ledRed               |
|3	| `Blink <id>`		          |Control device flashes.		          | LED, I2CLCD, CDS	           | `Blink` lelRed, `Blink` lcd   |
|4	| `Beep <id>`		            |Emitted a Beep.				              | Buzzer			                 | `Beep` buzzer                 |
|5	| `Show <String>`	          |Display the string on the display device.	|LCD		                 | `Show` "Xin chao"             |


Notes: States will be changed only if an event occurs. So every connection must have an event, except connected to "end point" or "start point".

## Connection command syntax

| No. |	Syntax | Description | Component | Example |
|:--|:--------------------------|:-----------------------------------------|:-----------------------------|:------------------------------|
|1	| `<string> button pressed`	|Description of the keypress event.	       | keypad		                    | "Cancel" `button pressed`     |
|2	| `<id> push`	              |Receive a push-button event.		           | push button	                | button `push`                 |
|3	| `<string> send`	          |Send data via wifi.				               | wifi esp8266	                | "hello" `send`                |
|4	| `<string> received`	      |Receive to strings sent from other wifi.  | wifi esp8266	                | "hello" `received`            |
|5	| `<id> : Display`	        |Check the status of the device.	         | LED	                        | ledRed : `Display`            |
|6	| `<id> : Hidden`	          |Check the status of the device.	         | LED	                        | ledRed : `Hidden`             |

## Installation

### Requirement

  * JavaSE-1.8 or higher
  * [Eclipse Modeling Tools - Version 2019-12 (4.14.0) or higher](https://www.eclipse.org/downloads/packages/release/2019-12/r/eclipse-modeling-tools)
  * Micraspis plugin v0.1.0, [download here](#version)

### Install plugin

* Extract zip file
* Copy all files in plugs folder
* Pase to the eclipse installation folder: eclipse/plugins/
* Restart Eclipse IDE

## Version

* [0.1.0](releases/v0.1.0.zip)


<!--Local-->
[overview]: #overview
[command-syntax]: #command-syntax
[connection-command-syntax]: #connection-command-syntax
[examples]: iotwearable.examples
[installation]: #installation
[requirement]: #requirement

<!--Images-->
[gen.png]: iotwearable.docs/images/gen.png
[mainboard_editor_gui.png]: iotwearable.docs/images/Mainboard_Editor_GUI.png
[stateschema_editor_gui.png]: iotwearable.docs/images/StateSchema_Editor_GUI.png
