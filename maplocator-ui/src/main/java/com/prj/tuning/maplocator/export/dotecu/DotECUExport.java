package com.prj.tuning.maplocator.export.dotecu;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.prj.tuning.maplocator.export.dotecu.model.ECUConfigData;
import com.prj.tuning.maplocator.export.ecuvars.model.*;
import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.model.LocatedMap.Endianness;
import com.prj.tuning.maplocator.plugin.PluginManager;
import com.prj.tuning.maplocator.util.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class DotECUExport {

	private static Logger log = new Logger(DotECUExport.class);
	private static String retval;
	
	public static void export(File f, Collection<LocatedMap> locatedMaps, ECUConfigData ecuData) throws JAXBException {
		retval = "";
		
		addLine("[Version]");
		addLine("Version = " + ecuData.version);
		addLine();
		
		addLine("[Communication]");
		addLine("Connect = " + ecuData.connect);
		addLine("Communicate = " + ecuData.communicate);
		addLine("LogSpeed = " + ecuData.logSpeed);
		
		addLine("[Identification]");
		addLine("HWNumber = {" + ecuData.hwNumber + "}");
		addLine("SWNumber = {" + ecuData.swNumber + "}");
		addLine("PartNumber = {" + ecuData.partNumber + "}");
		addLine("SWVersion = {" + ecuData.swVersion + "}" + ecuData.lang);
		addLine("EngineId = {" + ecuData.engineId + "}");
		addLine();
		
		addLine("[Measurements]");
		addLine("; Conversion factors:");
		addLine(";   S -> 0 = unsigned, 1 = signed value");
		addLine(";   I -> 0 = normal, 1 = inverse conversion");
		addLine(";   A -> factor");
		addLine(";   B -> offset");
		addLine("; Normal conversion:  phys = A * internal - B");
		addLine("; Inverse conversion: phys = A / (internal - B)");
		addLine();
		
		addLine(";Name           , {Alias}                           , Address, Size, Bitmask, {Unit},    S, I,            A,      B, Comment");
		
		ArrayList<LocatedMap> lm = new ArrayList<LocatedMap>(locatedMaps);
		Collections.sort(lm);
		
		for (LocatedMap l : lm) {
			if (l.isEcuvar())
			{
				add(getString(l.getId(), 16) + ", ");
				add(getString("{}", 34) + ", ");
				add(getString("0x" + Integer.toHexString(l.getAddress()).toUpperCase(), 8) + ", ");
				add(" " + l.getWidth() + ", ");
				add(" 0x0000, ");
				add(getString("{" + l.getUnits().substring(0, Math.min(l.getUnits().length(), 10)) + "}", 10) + ", ");
				add((l.isSigned() ? 1 : 0) + ", ");
				add((l.getEndianness() == Endianness.LITTLEENDIAN ? 1 : 0) + ", ");
				String factor = "" + l.getFactor();
				add(padString(factor.substring(0, Math.min(factor.length(), 12)), 12) + ", ");
				String offset = "" + l.getOffset();
				add(padString(offset.substring(0, Math.min(offset.length(), 6)), 6) + ", ");
				addLine("{" + l.getDesc() + "}");
			}
		}
		
		try
		{
			FileWriter fw = new FileWriter(f);
			fw.write(retval);
			fw.close();
		}
		catch (Exception ex)
		{
			
		}
	}
	
	private static void add(String s)
	{
		retval += s;
	}
	
	private static void addLine()
	{
		retval += "\r\n";
	}
	
	private static void addLine(String s)
	{
		retval += s + "\r\n";
	}
	
	private static String padString(String string, int length) {
	    return String.format("%1$"+length+ "s", string);
	}
	
    public static String getString(String str, int length) {
        for (int i = str.length(); i < length; i++)
            str += " ";
        return str;
    }
	
}
