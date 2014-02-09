package com.prj.tuning;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.prj.tuning.mappack.Project;
import com.prj.tuning.olsproject.OlsProject;

public class ProjectFactory {
  private static final Map<String, Class<? extends Project>> parsers = new HashMap<String, Class<? extends Project>>();
  
  static {
    parsers.put("kp", Project.class);
    parsers.put("ols", OlsProject.class);
  }
  
  public static Project getProject(URL projectUrl) throws Exception {
    String[] split = projectUrl.getFile().split("\\.");
    Class<? extends Project> parser = parsers.get(split[split.length-1].toLowerCase());
    
    if (parser == null) {
      throw new IllegalStateException("No parser found for file of type '." + split[split.length - 1] + "'!");
    }
    
    return parser.getConstructor(URL.class).newInstance(projectUrl);
  }
}
