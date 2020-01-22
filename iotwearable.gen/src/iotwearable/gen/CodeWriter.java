package iotwearable.gen;

import iotwearable.gen.utilities.GenLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Write the code to the marked position.
 */
public class CodeWriter {
	//Replace tag
	public static final String  projectTag = "<project_iotw>";
	public static final String includeTag= "<include_iotw>";
	public static final String desStateTag= "<desState_iotw>";
	public static final String defineTag = "<define_iotw>";
	public static final String prototypeTag = "<prototype_iotw>";
	public static final String setupTag= "<setup_iotw>";
	public static final String loopTag = "<loop_iotw>";
	public static final String implTag= "<impl_iotw>";
	
	private String sourceCode ;
	
	public CodeWriter() {
		this.sourceCode = getCodeTemplate();
	}
	
	/**
	 * Replace the content into the source code in place of the replaced tag.
	 * 
	 * @param sourceCode
	 *            is the source to be replaced.
	 * @param content
	 *            is the replacement.
	 * @param tagRep replaced.
	 * @return The source code has been written.
	 */
	public void write( String content, String tagRep)
	{
		if(content != null && tagRep != null)
			if (!content.isEmpty() && !tagRep.isEmpty()) {
				if(tagRep.equals(prototypeTag) || tagRep.equals(implTag)){
					if(sourceCode.contains(content))
						return;
				}
				this.sourceCode = sourceCode.replace(tagRep, content + "\n" + tagRep);
			}
	}
	
	public void cleanTag(){
		sourceCode = sourceCode.replaceAll(projectTag,"");
		sourceCode = sourceCode.replaceAll(includeTag,"");
		sourceCode = sourceCode.replaceAll(desStateTag,"");
		sourceCode = sourceCode.replaceAll(defineTag,"");
		sourceCode = sourceCode.replaceAll(prototypeTag,"");
		sourceCode = sourceCode.replaceAll(setupTag,"");
		sourceCode = sourceCode.replaceAll(loopTag,"");
		sourceCode = sourceCode.replaceAll(implTag,"");
	}
	
	private String getCodeTemplate() {
		try {
			String line = null;
			String result = "";
			InputStream in = getClass().getResourceAsStream("/templates/arduino_code_template.txt"); 
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			while ((line = bufferedReader.readLine()) != null) {
				result += line + "\n";
			}
			bufferedReader.close();
			return result;
		} catch (IOException e) {
			GenLogger.addLog("Error: Not found Arduino code template.");
		}
		return "";
	}

	public String getSourceCode() {
		return sourceCode;
	}
}
