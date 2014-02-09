package com.prj.tuning.maplocator.plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PatternMatcher {
  private static final Map<byte[], Map<String, Integer>> cache = Collections.synchronizedMap(new HashMap<byte[], Map<String, Integer>>());

  public static void clearCache() {
    cache.clear();
  }
  
  public static int findPattern(String pattern, byte[] binary) {
    // Check in cache
    if (cache.get(binary) != null) {
      Integer matchpos = cache.get(binary).get(pattern);
      if (matchpos != null) {
        return matchpos;
      }
    }
    
    int matchpos = 0;
    String[] tokens = pattern.split(" ");
    
    while (matchpos < binary.length) {
      int result = patternMatches(tokens, binary, matchpos);
      if (result != -1) {
        if (result > 0) matchpos = result;
        break;
      }
      matchpos++;
    }
    
    matchpos = matchpos == binary.length ? -1 : matchpos;
    
    if (cache.get(binary) == null) {
      cache.put(binary, Collections.synchronizedMap(new HashMap<String, Integer>()));
    }
    
    cache.get(binary).put(pattern, matchpos);

    return matchpos;
  }
  
  private static int patternMatches(String[] tokens, byte[] binary, int offset) {
    int curpos = offset;
    int marker = -1;
    
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      
      if (tokens[i].startsWith("MM")) {
        token = tokens[i].substring(2);
        marker = curpos;
      }
      
      if (token.equals("XX")) {
        curpos++;
        continue;
      }
      
      if (token.startsWith("XX")) {
        int maxLen = Integer.parseInt(token.substring(2));
        int pos = curpos;
        while (maxLen >= 0) {
          int result = patternMatches(Arrays.copyOfRange(tokens, i + 1, tokens.length), binary, pos);
          if (result != -1) {
            return marker == -1 ? result : marker;
          } else {
            pos++;
            maxLen--;
          }
        }
        return -1;
      }
      
      if ((byte) (Integer.parseInt(token, 16)) != binary[curpos]) {
        return -1;
      } else {
        curpos++;
      }
    }
    
    return marker == -1 ? 0 : marker;
  }
}
