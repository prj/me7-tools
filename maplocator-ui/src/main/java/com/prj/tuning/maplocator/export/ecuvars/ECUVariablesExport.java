package com.prj.tuning.maplocator.export.ecuvars;

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

import com.prj.tuning.maplocator.export.ecuvars.model.*;
import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.plugin.PluginManager;
import com.prj.tuning.maplocator.util.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class ECUVariablesExport {

	private static Logger log = new Logger(ECUVariablesExport.class);
	
	/*Serialization export - not working
	public static void export(File f, Collection<LocatedMap> locatedMaps) throws JAXBException {
		
		JAXBContext ctx = JAXBContext.newInstance(ECUVariablesProject.class);
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-15");

		marshaller.marshal(new ECUVariablesProject(locatedMaps), f);

	}*/
	
	public static void export(File f, Collection<LocatedMap> locatedMaps, String swNumber) throws JAXBException {
    	String rootElement = "ECUParameters";
    	
    	Namespace rootNs = new Namespace("", "http://www.volvotuners.com");
    	QName rootQName = QName.get(rootElement, rootNs);
    	
		Document d = DocumentHelper.createDocument();
		Element ecuparams = d.addElement(rootElement);
		ecuparams.setQName(rootQName);
		ecuparams.addAttribute("xmlns:i", "http://www.w3.org/2001/XMLSchema-instance");
		Element ecuvars = ecuparams.addElement("ECUVariables");
		ArrayList<LocatedMap> lm = new ArrayList<LocatedMap>(locatedMaps);
		Collections.sort(lm);
		
		for (LocatedMap l : lm) {
			if (l.isEcuvar())
			{
				ECUVariable v = ecuvarFromLocatedMap(l);
				Element e = ecuvars.addElement("ECUVariable");
				e.addElement("address").addText("0x" + String.valueOf(v.address).toUpperCase());
				e.addElement("desc").addText(String.valueOf(v.desc));
				e.addElement("factor").addText(String.valueOf(v.factor));
				e.addElement("name").addText(String.valueOf(v.name));
				e.addElement("offset").addText(String.valueOf(v.offset));
				e.addElement("precision").addText(String.valueOf(v.precision));
				e.addElement("signed").addText(String.valueOf(v.signed));
				e.addElement("units").addText(String.valueOf(v.units));
				e.addElement("word").addText(String.valueOf(v.word));
			}
		}
		ecuparams.addElement("swNumber").addText(swNumber);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(prettyFormat(d.asXML()));
			fw.close();
		}
		catch (Exception ex)
		{
			
		}
	}
	
	public static ECUVariable ecuvarFromLocatedMap(LocatedMap l) {
		return new ECUVariable(l.getId(), l.getDesc(), Integer.toHexString(l
				.getAddress()), l.getUnits(), (l.getWidth() == 2 ? true : false),
				l.isSigned(), l.getFactor(), l.getOffset(), l.getPrecision());
	}
	
	
	//XML Pretty Print Formatter
	//http://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
    public static String prettyFormat(String input) {
    	int indent = 2;
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer(); 
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }
}
