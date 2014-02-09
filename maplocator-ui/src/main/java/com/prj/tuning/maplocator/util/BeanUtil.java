package com.prj.tuning.maplocator.util;

import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
  private final static Map<Class<?>, Class<?>> wrapperMap = new HashMap<Class<?>, Class<?>>();
  static {
      wrapperMap.put(boolean.class, Boolean.class);
      wrapperMap.put(byte.class, Byte.class);
      wrapperMap.put(short.class, Short.class);
      wrapperMap.put(char.class, Character.class);
      wrapperMap.put(int.class, Integer.class);
      wrapperMap.put(long.class, Long.class);
      wrapperMap.put(float.class, Float.class);
      wrapperMap.put(double.class, Double.class);
      
      wrapperMap.put(Boolean.class, boolean.class);
      wrapperMap.put(Byte.class, byte.class);
      wrapperMap.put(Short.class, short.class);
      wrapperMap.put(Character.class, char.class);
      wrapperMap.put(Integer.class, int.class);
      wrapperMap.put(Long.class, long.class);
      wrapperMap.put(Float.class, float.class);
      wrapperMap.put(Double.class, double.class);
  }
  
  public static void transferValue(Object fromBean, Object toBean, String fromField, String toField, Object defValue) throws Exception {
    Object value = fromBean;
    
    if (value != null) {
      String[] fromFieldArr = fromField.split("\\.");
      for (String curField : fromFieldArr) {
        value = value.getClass().getMethod("get" + curField.substring(0, 1).toUpperCase() + curField.substring(1)).invoke(value);
        if (value == null) {
          value = defValue;
          break;
        }
      }
    } else {
      value = defValue;
    }
    
    String methodName = "set" + toField.substring(0, 1).toUpperCase() + toField.substring(1);
    
    try {
      toBean.getClass().getMethod(methodName, value.getClass()).invoke(toBean, value);
    } catch (NoSuchMethodException e) {
      if (wrapperMap.containsKey(value.getClass())) {
        toBean.getClass().getMethod(methodName, wrapperMap.get(value.getClass())).invoke(toBean, value);
      } else {
        throw e;
      }
    }
  }
}
