package com.prj.tuning.maplocator.model;

import java.util.HashSet;
import java.util.Set;

import com.prj.tuning.mappack.Project;
import com.prj.tuning.mappack.map.PMap;

public class LocatedMap {
  private PMap map;
  private int locatedAddress;
  private byte[] source;
  private byte[] target;
  private Project project;
  private Set<String> ids = new HashSet<String>();
  private int xAxisAddr;
  private int yAxisAddr;
  
  public LocatedMap(PMap map, int address, byte[] source, Project project, byte[] target) {
    this.map = map;
    this.locatedAddress = address;
    this.source = source;
    this.project = project;
    this.target = target;
  }

  public PMap getMap() {
    return map;
  }

  public int getLocatedAddress() {
    return locatedAddress;
  }

  public byte[] getSource() {
    return source;
  }

  public byte[] getTarget() {
    return target;
  }

  public Project getProject() {
    return project;
  }

  public Set<String> getIds() {
    return ids;
  }

  public int getxAxisAddr() {
    return xAxisAddr;
  }

  public void setxAxisAddr(int xAxisAddr) {
    this.xAxisAddr = xAxisAddr;
  }

  public int getyAxisAddr() {
    return yAxisAddr;
  }

  public void setyAxisAddr(int yAxisAddr) {
    this.yAxisAddr = yAxisAddr;
  }
}
