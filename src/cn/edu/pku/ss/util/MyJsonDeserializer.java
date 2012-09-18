package cn.edu.pku.ss.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class MyJsonDeserializer implements JsonDeserializer<Object> {
	 @Override
     public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       JsonPrimitive value = json.getAsJsonPrimitive();
       if (value.isBoolean()) {
         return value.getAsBoolean();
       } else if (value.isNumber()) {
         return value.getAsNumber();
       } else if(value.isString()){
         return value.getAsString();
       }else{
       	return handleArray(value.getAsJsonArray(), context);
       }
     }
     
     private Object handleArray(JsonArray json, JsonDeserializationContext context) {
   	    Object[] array = new Object[json.size()];
   	    for(int i = 0; i < array.length; i++)
   	      array[i] = context.deserialize(json.get(i), Object.class);
   	    return array;
   	  }
     
     private Object handleObject(JsonObject json, JsonDeserializationContext context) {
    	    Map<String, Object> map = new HashMap<String, Object>();
    	    for(Map.Entry<String, JsonElement> entry : json.entrySet())
    	      map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
    	    return map;
    	  }
}
