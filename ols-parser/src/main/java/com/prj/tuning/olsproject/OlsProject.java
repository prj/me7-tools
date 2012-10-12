package com.prj.tuning.olsproject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.prj.tuning.mappack.Folder;
import com.prj.tuning.mappack.Project;
import com.prj.tuning.mappack.map.PMap;
import com.prj.tuning.mappack.util.BinaryUtil;
import com.prj.tuning.olsproject.map.OlsPMap;

public class OlsProject extends Project {
  
  public OlsProject(URL projectUrl) {
    super(projectUrl);
  }

  @Override
  public Project parse() throws IOException {
    projectData = BinaryUtil.readFile(projectUrl);
    BinaryUtil.skip(projectData, 1372);
    maps = new HashSet<PMap>();
    
    try {
      while (true) {
        maps.add(new OlsPMap().parse(projectData));
      }
    } catch (IllegalStateException e) {
      header = new OlsHeader(maps.size());
      return this;
    }
  }
  
  public static void main(String[] args) throws Exception {
    new OlsProject(new File("/Users/gm/10000.ols").toURI().toURL()).parse();
  }

  @Override
  public Collection<Folder> getFolders() {
    return Collections.emptyList();
  }
  
}
