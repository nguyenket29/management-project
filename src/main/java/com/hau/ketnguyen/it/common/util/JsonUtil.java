/*******************************************************
 * Copyright by Dsoft - All rights reserved.  *    
 *******************************************************/
package com.hau.ketnguyen.it.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class JsonUtil {

  private static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();
    MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  public static <O> String toJson(O o) {
    try {
      return MAPPER.writeValueAsString(o);
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public static <T> T toObject(String jsonStr, final Class<T> clazz) {
    try {
      return MAPPER.readValue(jsonStr, clazz);
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> T toObject(String jsonStr, final TypeReference<T> reference) {
    try {
      return MAPPER.readValue(jsonStr, reference);
    } catch (Exception e) {
      return null;
    }
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

}
