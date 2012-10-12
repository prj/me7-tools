package com.prj.tuning.maplocator.util;

public class Logger {
  private Class<?> klazz;
  public Logger(Class<?> klazz) {
    this.klazz = klazz;
  }
  
  public void log(String message) {
    System.out.println("[" + klazz.getSimpleName() + "] " + message);
  }
}
