package com.why.tool.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

public class JSONUtils {
	
	private static ObjectMapper om = new ObjectMapper();
	private static ObjectMapper ignoreUnknownPropertiesObjectMapper = new ObjectMapper();
	static{
		ignoreUnknownPropertiesObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		
		SerializationConfig serializationConfig = ignoreUnknownPropertiesObjectMapper.copySerializationConfig();
		serializationConfig.setSerializationInclusion(Inclusion.NON_NULL);
		ignoreUnknownPropertiesObjectMapper.setSerializationConfig(serializationConfig);
	}
	
	public static <T> String toJSONwithOutNullProp(T object) {
		try {
			return ignoreUnknownPropertiesObjectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
	}
	
	public static <T> String toJSON(T object) {
		ObjectMapper mapper = getObjectMapper(false);
		try {
			String jsonStr = mapper.writeValueAsString(object);
			return jsonStr;
		} catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
	}
	
	
	public static <T> String toJSONWithQuoteFieldName(T object) {
		return removeQuoteFromFieldName(toJSON(object));
	}
	public static String removeQuoteFromFieldName(String jsonStr) {
		
		char[] charArray = jsonStr.toCharArray();
		char[] result = new char[charArray.length];
		int index = 0;
		char c;
		int leftQ = -1;
		int rightQ = -1;
		boolean findingLeftQ = true;
		for (int i = 0; i < charArray.length; i++) {
			c = charArray[i];
			if (':'==c){
				//删掉leftQ和rightQ所在字符
				//倒数第几个位置
				int leftQ4Result = index - (i - leftQ);
				int rightQ4Result = index - (i - rightQ);
				index = removeChar(result, leftQ4Result, index);
				index = removeChar(result, rightQ4Result, index);
				
			}else if ('\"'==c){
				//计算leftQ和rightQ所在位置
				if(findingLeftQ){
					leftQ = i;
					findingLeftQ = false;
				}else{
					rightQ = i;
					findingLeftQ = true;
				}
			}
			//拷贝当前字符
			result[index++] = charArray[i];
		}
		
		return String.valueOf(result).substring(0, index);
	}
	public static int removeChar4Test(char[] result, int position, int index) {
		return removeChar(result, position, index);
	}
	private static int removeChar(char[] result, int position, int index) {
		for (int i = position; i < index; i++) {
			result[i] = result[i+1];
		}
		result[index] = ' ';
		return index - 1;
	}

	/**
	 * 
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJSON(String jsonString, Class<T> clazz) {
		
		ObjectMapper mapper = getObjectMapper();
		T object = null;
		try {
			object = mapper.readValue(jsonString, clazz);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
		return object;
	}

	
	public static <T> T fromJSON(String jsonString, Class<T> clazz,boolean isIgnoreUnknownProperties) {
		ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);
		T object = null;
		try {
			object = mapper.readValue(jsonString, clazz);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
		return object;
	}
	
	public static <T> T fromJSON(String jsonString, TypeReference<T> typeReference, boolean isIgnoreUnknownProperties) {
		ObjectMapper mapper = getObjectMapper(isIgnoreUnknownProperties);
		T object = null;
		try {
			object = (T) mapper.readValue(jsonString, typeReference);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
		return object;
	}
	
	public static <T> List<T> fromJSONToList(String jsonString, Class<T> clazz) {
		return fromJSONToList(jsonString,clazz,false);
	}
	
	
	
	public static <T> List<T> fromJSONToList(String jsonString, Class<T> clazz,boolean isIgnoreUnknownProperties) {
		
		ObjectMapper mapper = getObjectMapper();
		try {
			 JsonNode  rootNode = mapper.readTree(jsonString);
			 Iterator<JsonNode> it=  rootNode.getElements();
			 List<T> list =  new ArrayList<T>();
			 while(it.hasNext()){
				 T t = fromJSON(it.next().toString(),clazz);  
				 list.add(t);
			 }
			 return list;
		}catch (JsonGenerationException e) {
			throw new RuntimeException("JsonGenerationException",e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("JsonMappingException",e);
		} catch (IOException e) {
			throw new RuntimeException("IOException",e);
		}
	}
	/**
	 * FIXME: share the object.
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return  getObjectMapper(true) ;
	}
	
	public static ObjectMapper getObjectMapper(boolean isIgnoreUnknownProperties) {
		return isIgnoreUnknownProperties   ?  ignoreUnknownPropertiesObjectMapper : om;
	}

}
