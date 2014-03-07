package com.prj.tuning.maplocator.model;

import java.util.Collection;
import java.util.Collections;

import com.prj.tuning.mappack.Folder;
import com.prj.tuning.mappack.Header;
import com.prj.tuning.mappack.Project;
import com.prj.tuning.mappack.map.PMap;

public class DummyProject extends Project {
  private Collection<PMap> maps;
  
  public DummyProject(Collection<PMap> maps) {
    super(null);
    this.maps = maps;
  }

  @Override
  public Header getHeader() {
    return new DummyHeader("definition");
  }

  @Override
  public Collection<PMap> getMaps() {
    return maps;
  }

  @Override
  public Collection<Folder> getFolders() {
    return Collections.emptyList();
  }
  
}
