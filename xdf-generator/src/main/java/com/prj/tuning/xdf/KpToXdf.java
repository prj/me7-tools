package com.prj.tuning.xdf;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.prj.tuning.mappack.Project;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.olsimpl.OlsProject;

public class KpToXdf {

  public static void main(String[] args) throws Exception {
    Project p = new com.prj.tuning.olsproject.OlsProject(new File(args[0]).toURI().toURL()).parse();

    JAXBContext ctx = JAXBContext.newInstance(XdfProject.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-15");

    marshaller.marshal(new OlsProject(p), System.out);
  }

}
