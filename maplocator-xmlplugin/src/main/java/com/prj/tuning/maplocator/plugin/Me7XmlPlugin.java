package com.prj.tuning.maplocator.plugin;

import java.io.File;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import com.prj.tuning.maplocator.model.LocatedMap;
import com.prj.tuning.maplocator.model.LocatedMap.Endianness;
import com.prj.tuning.maplocator.plugin.jaxb.Address;
import com.prj.tuning.maplocator.plugin.jaxb.Map;
import com.prj.tuning.maplocator.util.BeanUtil;
import com.prj.tuning.maplocator.util.Logger;

public class Me7XmlPlugin implements LocatorPlugin {
  private static Logger log = new Logger(Me7XmlPlugin.class);

  public Collection<? extends LocatedMap> locateMaps(byte[] binary) {
    PatternMatcher.clearCache();
    HashMap<String, LocatedMapWithXml> maps = new HashMap<String, LocatedMapWithXml>();

    try {
      JAXBContext ctx = JAXBContext.newInstance("com.prj.tuning.maplocator.plugin.jaxb");
      Unmarshaller unmarshaller = ctx.createUnmarshaller();

      File xmls;
      if (System.getProperty("xml.dir") != null) {      
        xmls = new File(System.getProperty("xml.dir"));
      } else {
        xmls = new File("me7xmls");
      }
      
      HashSet<String> axisIds = new HashSet<String>();
      
      for (File file : xmls.listFiles()) {
        if (file.getName().toLowerCase().endsWith(".xml")) {
          Map map = ((JAXBElement<Map>) unmarshaller.unmarshal(file)).getValue();
          if (map.getColAxis() != null) {
            axisIds.addAll(map.getColAxis().getId());
          }
          
          if(map.getRowAxis() != null) {
            axisIds.addAll(map.getRowAxis().getId());
          }
          
          LocatedMapWithXml lMap = getLocatedMap(map, binary);
          if (lMap != null) {
            maps.put(lMap.getId(), lMap);
          }
        }
      }
      
      // Add axes
      for (Iterator<String> i = maps.keySet().iterator(); i.hasNext();) {
        LocatedMapWithXml lMap = maps.get(i.next());
        if (axisIds.contains(lMap.getId())) {
          lMap.setAxis(binary);
        }
        
        if (lMap.map.getColAxis() != null) {
          for (String id : lMap.map.getColAxis().getId()) {
            LocatedMapWithXml xAxis = maps.get(id);
            if (xAxis != null) {
              lMap.setxAxis(xAxis);
              break;
            }
          }
          
          if (lMap.getxAxis() == null) {
            log.log("Removing " + lMap.getId() + ", because col axis was not found.");
            i.remove();
            continue;
          }
        }
        
        if (lMap.map.getRowAxis() != null) {
          for (String id : lMap.map.getRowAxis().getId()) {
            LocatedMapWithXml yAxis = maps.get(id);
            if (yAxis != null) {
              lMap.setyAxis(yAxis);
              break;
            }
          }
          
          if (lMap.getyAxis() == null) {
            log.log("Removing " + lMap.getId() + ", because row axis was not found.");
            i.remove();
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return maps.values();
  }
  
  private static int getInt(byte[] binary, int offset) {
    return new BigInteger(new byte[] { binary[offset + 1], binary[offset] }).intValue() & 0x0000FFFF;
  }

  private static LocatedMapWithXml getLocatedMap(Map map, byte[] binary) throws Exception {
    
    // Find pattern
    int patternLocation = PatternMatcher.findPattern(map.getPattern(), binary);
    
    if (patternLocation == -1) {
      return null;
    } else {
      log.log("Pattern for " + map.getId() + " found at: " + String.format("0x%X", patternLocation));
    }

    int address = getAddress(binary, map.getAddress(), patternLocation);

    LocatedMapWithXml lMap = new LocatedMapWithXml();
    lMap.map = map;
    lMap.setId(map.getId());
    lMap.setAddress(address);

    // Conversion
    BeanUtil.transferValue(map, lMap, "conversion.factor", "factor", 1.0);
    BeanUtil.transferValue(map, lMap, "conversion.offset", "offset", 0.0);
    BeanUtil.transferValue(map, lMap, "conversion.width", "width", 1);

    if (map.getConversion() != null && "LoHi".equals(map.getConversion().getEndianness())) {
      lMap.setEndianness(Endianness.BIGENDIAN);
    }
    else {
      lMap.setEndianness(Endianness.LITTLEENDIAN);
    }

    BeanUtil.transferValue(map, lMap, "conversion.signed", "signed", false);
    
    // Length
    if (map.getLength() != null) {
      if (map.getLength().getHardcoded() != null) {
        lMap.setLength(map.getLength().getHardcoded().intValue());
      } else {
        int lenAddr = getAddress(binary, map.getLength().getAddress(), patternLocation);
        int len = map.getLength().getWidth() == null ? 1 : map.getLength().getWidth();
        lMap.setLength(len == 1 ? binary[lenAddr] & 0xFF : getInt(binary, lenAddr));
      }
    }
    
    return lMap;
  }
  
  private static int getAddress(byte[] binary, Address address, int patternLocation) {
    int dpp = 0x204;
    int addr = 0;
    if (address != null) {
      if (address.getDpp() != null) {
        dpp = new BigInteger(address.getDpp()).intValue() & 0x0000FFFF;
      }
      if (address.getDppOffset() != null) {
        int loc = patternLocation + address.getDppOffset();
        dpp = getInt(binary, loc);
      }
    }

    // Address
    if (address != null && address.getOffset() != null) {
      addr = getInt(binary, patternLocation + address.getOffset());
    }
    else {
      addr = getInt(binary, patternLocation);
    }

    return dpp * 0x4000 - 0x800000 + addr;
  }
  
  public static class LocatedMapWithXml extends LocatedMap {
    private Map map;
    
    private void setAxis(byte[] binary) {
      setAxis(true);
      if (map.getLength() == null) {
        setLength(getWidth() == 1 ? binary[getAddress()] & 0xFF : getInt(binary, getAddress()));
        setAddress(getAddress() + getWidth());
      }
    }
  }
}
