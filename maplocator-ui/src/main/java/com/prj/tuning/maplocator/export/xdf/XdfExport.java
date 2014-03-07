package com.prj.tuning.maplocator.export.xdf;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.prj.tuning.maplocator.export.xdf.model.Project;
import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.xdf.binding.XdfProject;

public class XdfExport {

	public static void export(File f, Collection<LocatedMap> locatedMaps,
			int size) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(XdfProject.class);
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-15");

		marshaller.marshal(new Project(getMaps(locatedMaps), size), f);
	}

	public static Collection<LocatedMap> getMaps(Collection<LocatedMap> locatedMaps) {
		Collection<LocatedMap> noECUVars = new HashSet<LocatedMap>();
		for (LocatedMap l : locatedMaps) {
			if (!l.isEcuvar()) {
				noECUVars.add(l);
			}
		}
		return noECUVars;
	}

}
