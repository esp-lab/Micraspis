package iotwearable.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import iotwearable.gen.utilities.GenLogger;
import iotwearable.model.iotw.BluetoothHC06;
import iotwearable.model.iotw.Button;
import iotwearable.model.iotw.Buzzer;
import iotwearable.model.iotw.Device;
import iotwearable.model.iotw.I2CLCD;
import iotwearable.model.iotw.Keypad4x4;
import iotwearable.model.iotw.LED;
import iotwearable.model.iotw.LM35;
import iotwearable.model.iotw.Mainboard;
import iotwearable.model.iotw.Pin;
import iotwearable.model.iotw.WifiESP8266;

public class Manual {
	private Mainboard mainboard;
	String html;

	public Manual(String pathMaiboard) {
		this.mainboard = Convert.convertToMainboard(pathMaiboard);
		this.html = "";
	}

	public String createManual(String title) {
		try {
			String line = null;
			InputStream in = getClass().getResourceAsStream("/templates/manual.html"); 
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			while ((line = bufferedReader.readLine()) != null) {
				html += line + "\n";
			}
			bufferedReader.close();
		} catch (IOException e) {
			GenLogger.addLog("Error: Not found manual code template.");
		}
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
		String s = timeFormat.format(today.getTime());
		html = html.replace("<!--_title-->", title);
		html = html.replace("<!--dateGenCode-->", s);
		html = html.replace("<!--device-->", genListControl());
		html = html.replace("<!--details-->", genManualConnect());
		return html;
	}

	/**
	 * generate table Manual connect of project
	 * 
	 * @return table Manual connect
	 */
	private String genManualConnect() {
		String manual = "";

		for (Device device : mainboard.getDevices()) {
			for (Pin pin : device.getPinConnecteds()) {
				manual += "<tr>\n" + "<td>" + device.getName() + "</td>\n" + "<td>" + pin.getName() + "</td>\n" + "<td>"
						+ getPinMainboard(pin.getIdConnect()) + "</td>\n" + "</tr>\n";
			}
		}
		for (Device device : mainboard.getDevices()) {
			if (device instanceof Keypad4x4) {
				manual = replaceManual(manual, device.getName(), 8);
			}
			if (device instanceof I2CLCD) {
				manual = replaceManual(manual, device.getName(), 2);
			}
			if (device instanceof BluetoothHC06) {
				manual = replaceManual(manual, device.getName(), 2);
			}
			if (device instanceof WifiESP8266) {
				manual = replaceManual(manual, device.getName(), 2);
			}
			if (device instanceof LM35) {
				manual = replaceManual(manual, device.getName(), 1);
			}
		}
		return manual;
	}

	/**
	 * get name of pin of mainboard with idConnect
	 * 
	 * @param idConnect
	 * @return name of pin of mainboard
	 */
	private String getPinMainboard(String idConnect) {
		for (Pin pin : mainboard.getPinConnecteds()) {
			if (pin.getIdConnect().equals(idConnect))
				return pin.getName();
		}
		return "";
	}

	/**
	 * merge rows name of device into one row
	 * 
	 * @param content   : source manual(html source)
	 * @param name      : name device replace
	 * @param rowNumber : row replace of html source
	 * @return manual file
	 */
	private String replaceManual(String content, String name, int rowNumber) {
		content = content.replace("<td>" + name + "</td>", "<!--" + name + "-->");
		content = content.replaceFirst("<!--" + name + "-->", "<td rowspan=" + rowNumber + ">" + name + "</td>\n");
		return content;
	}

	/**
	 * generate list device declare in Mainboard Editor
	 * 
	 * @return table of list Device
	 */
	private String genListControl() {
		String listControl = "";
		int countLED = 0, countLCD = 0, countKeypad = 0, countBuzzer = 0, countBluetooth = 0, countWifi = 0,
				countButton = 0, countTransistor = 0, countLM35 = 0;
		String typeLED = "", typeLCD = "", typeKeypad = "", typeBuzzer = "", typeBluetooth = "", typeWifi = "",
				typeButton = "", typeLM35 = "";
		for (Device device : mainboard.getDevices()) {
			if (device instanceof LED) {
				countLED++;
				typeLED = device.getName();
				countTransistor++;
			}

			else if (device instanceof I2CLCD) {
				typeLCD = device.getName();
				countLCD++;
			} else if (device instanceof Keypad4x4) {
				typeKeypad = device.getName();
				countKeypad++;
			} else if (device instanceof Buzzer) {
				typeBuzzer = device.getName();
				countBuzzer++;
				countTransistor++;
			} else if (device instanceof Button) {
				typeButton = device.getName();
				countButton++;
				countTransistor++;
			}
			if (device instanceof BluetoothHC06) {
				countBluetooth++;
				typeBluetooth = device.getName();
			} else if (device instanceof WifiESP8266) {
				countWifi++;
				typeWifi = device.getName();
				countTransistor++;
			} else if (device instanceof LM35) {
				countLM35++;
				typeLM35 = device.getName();
				countTransistor++;
			}
		}
		String mainboardName = mainboard.getName().equals("Arduino WIFI ESP8266 WEMOS D1") ? mainboard.getName() + " R2"
				: mainboard.getName();
		listControl += "<tr>\n" + "<td>" + mainboardName + "</td>\n" + "<td>" + 1 + "</td>\n" + "</tr>\n";
		if (countBuzzer != 0)
			listControl += "<tr>\n" + "<td>" + typeBuzzer + "</td>\n" + "<td>" + countBuzzer + "</td>\n" + "</tr>\n";
		if (countKeypad != 0)
			listControl += "<tr>\n" + "<td>" + typeKeypad + "</td>\n" + "<td>" + countKeypad + "</td>\n" + "</tr>\n";
		if (countLCD != 0)
			listControl += "<tr>\n" + "<td>" + typeLCD + "</td>\n" + "<td>" + countLCD + "</td>\n" + "</tr>\n";
		if (countLED != 0)
			listControl += "<tr>\n" + "<td>" + typeLED + "</td>\n" + "<td>" + countLED + "</td>\n" + "</tr>\n";
		if (countWifi != 0)
			listControl += "<tr>\n" + "<td>" + typeWifi + "</td>\n" + "<td>" + countWifi + "</td>\n" + "</tr>\n";
		if (countBluetooth != 0)
			listControl += "<tr>\n" + "<td>" + typeBluetooth + "</td>\n" + "<td>" + countBluetooth + "</td>\n"
					+ "</tr>\n";
		if (countButton != 0)
			listControl += "<tr>\n" + "<td>" + typeButton + "</td>\n" + "<td>" + countButton + "</td>\n" + "</tr>\n";
		if (countLM35 != 0)
			listControl += "<tr>\n" + "<td>" + typeLM35 + "</td>\n" + "<td>" + countLM35 + "</td>\n" + "</tr>\n";
		if (countTransistor != 0)
			listControl += "<tr>\n" + "<td>Transistor 220 Ohm</td>\n" + "<td>" + countTransistor + "</td>\n"
					+ "</tr>\n";
		return listControl;
	}

	public static boolean isWindows(String OS) {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac(String OS) {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix(String OS) {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris(String OS) {

		return (OS.indexOf("sunos") >= 0);

	}

}
