package com.prj.tuning.maplocator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.prj.tuning.ProjectFactory;
import com.prj.tuning.maplocator.locator.LocatorChain;
import com.prj.tuning.maplocator.model.DummyProject;
import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.mappack.Project;
import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.xdf.binding.XdfProject;
import com.prj.tuning.xdf.olsimpl.OlsProject;
import com.prj.tuning.xdf.olsimpl.addressmap.AddressMap;

public class Maplocator {

  public static void main(String[] args) throws Exception {
    Properties props = new Properties();
    FileInputStream fis = new FileInputStream("maplocator.ini");
    props.load(fis);
    fis.close();
    
    // Read OLS project
    Project p = ProjectFactory.getProject(new File(args[0]).toURI().toURL()).parse();
    
    // Read source bin
    File f = new File(args[1]);
    BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
    byte[] source = new byte[is.available()];
    is.read(source);
    is.close();
    
    // Read target bin
    f = new File(args[2]);
    is = new BufferedInputStream(new FileInputStream(f));
    byte[] target = new byte[is.available()];
    is.read(target);
    is.close();

    // Locate maps
    Collection<LocatedMap> locatedMaps = LocatorChain.getMaps(props, p, source, target);
    
    
    // Prepare for XDF
    final Map<Integer, Integer> addressMap = new HashMap<Integer, Integer>();
    Collection<PMap> xdfMaps = new HashSet<PMap>();

    for (LocatedMap map : locatedMaps) {
      addressMap.put(map.getMap().getAddress(), map.getLocatedAddress());
      if (map.getxAxisAddr() != 0) {
        addressMap.put(map.getMap().getxAxis().getAddress(), map.getxAxisAddr());
      }
      if (map.getyAxisAddr() != 0) {
        addressMap.put(map.getMap().getyAxis().getAddress(), map.getyAxisAddr());
      }
      
      xdfMaps.add(map.getMap());
    }
    
    OlsProject xdfProject = new OlsProject(new DummyProject(xdfMaps), new AddressMap() {
      public int subAddr(int addr) {
        Integer found = addressMap.get(addr);
        return found == null? addr : found;
      }
    });
    
    System.err.println("Generating XDF...");
    
    // Write XDF
    JAXBContext ctx = JAXBContext.newInstance(XdfProject.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-15");
    marshaller.marshal(xdfProject, System.out);
  }

}
