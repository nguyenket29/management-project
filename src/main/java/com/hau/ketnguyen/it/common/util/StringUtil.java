package com.hau.ketnguyen.it.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

  public static final String EMPTY = "";
  public static final String SPACE = " ";
  public static final String DOT = ".";
  public static final String COMMA = ",";
  public static final String COLON = ":";
  public static final String SEMICOLON = ";";
  public static final String UNDERSCORE = "_";

  public static boolean isEmpty(String... args) {
    for (String ele : args) {
      if (ele == null || ele.trim().isEmpty())
        return true;
    }
    return false;
  }

  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }
  
  public static String concatenate(List<String> listOfItems, String separator) {
    StringBuilder sb = new StringBuilder();
    Iterator<String> iterator = listOfItems.iterator();
    while (iterator.hasNext()) {
      sb.append(iterator.next());
      if (iterator.hasNext()) {
        sb.append(separator);
      }
    }
    return sb.toString();
  }

  public static String toStringFromList(List<String> list, String separator) {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < list.size(); i++) {
      sb.append(list.get(i));
      if(i < (list.size() -1)) {
        sb.append(separator); 
      }
    } 
    return sb.toString();
  }
  public static String convertDateToString(Date date, String pattern) {
    String dateStr = null;
    if (date != null) {
      DateFormat dateFormat = new SimpleDateFormat(pattern);
      try {
        dateStr = dateFormat.format(date);
      } catch (Exception e) {
        return null;
      }
    }
    return dateStr;
  }
}
