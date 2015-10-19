package com.why.tool.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class ParseCmemKeys {

	public static void main(String[] args) throws IOException {
		String path = "D:/迅雷下载/CMEM_Keys/txtFile";
		File rootDir = new File(path);
		if (!rootDir.exists()) {
			throw new IllegalArgumentException("文件" + rootDir.getPath() + "不存在");
		}
		
		printFileInfo(rootDir);
		
		System.out.println("\nfound " + totalFileNum + " file, totalSize=" + totalSize + " MB, maxSize=" + maxSize + " MB, avgSize=" + totalSize
				/ totalFileNum + " MB\n");
		
//		for(String pattern:patterns){
//			System.out.println(pattern);
//		}
		File targetFile = new File("D:/迅雷下载/CMEM_Keys/allKeys.csv");
		FileUtils.writeLines(targetFile, "GB2312", patternMap.values());
	}

	private static int totalFileNum = 0;
	private static double totalSize = 0;
	private static double maxSize = 0;

	private static void printFileInfo(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				printFileInfo(file);
			} else {
				double size = file.length() / 1024.0 / 1024.0;
				if (size > maxSize) {
					maxSize = size;
				}
				totalSize += size;
				totalFileNum++;
				System.out.println(totalFileNum+"："+file.getPath() + "\t" + size);
				parseKeys(file);
			}
		}
	}
	
	private static Map<String, KeyInfo> patternMap = new TreeMap<String, KeyInfo>();
	
	private static void parseKeys(File file){
		List<String> keys = null;
		try {
			keys = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String key:keys){
			char[] chars = key.toCharArray();
			StringBuilder sb = new StringBuilder();
			boolean numBegin = false;
			for(int i=0;i<chars.length;i++){
				char c = chars[i];
				if(isNum(c) || isHex(c)){
					numBegin = true;
					if(i == chars.length-1){
						sb.append('^');
					}
					continue;
				}else{
					if(numBegin){
						numBegin = false;
						sb.append('^');
					}
					sb.append(c);
				}
				
			}
			
			String mapKey = sb.toString();
			if(patternMap.containsKey(mapKey)){
				KeyInfo keyInfo = patternMap.get(mapKey);
				keyInfo.appearCount++;
				patternMap.put(mapKey, keyInfo);
			}else{
				patternMap.put(mapKey, new KeyInfo(mapKey, file.getPath(), 1));
			}
		}
	}
	
	private static class KeyInfo{
		String key;
		List<String> appearFileNames;
		int appearCount;
		public KeyInfo(String key, String appearFileName, int appearCount) {
			this.key = key;
			this.appearFileNames = new ArrayList<String>();
			appearFileNames.add(appearFileName);
			this.appearCount = appearCount;
		}
		@Override
		public String toString() {
			return key + "," + appearCount + "," + appearFileNames;
		}
		
	}
	
	private static boolean isNum(char c){
		return c >= 48 && c <= 57;
	}
	
	private static boolean isHex(char c){
		return c=='A' || c=='B' || c=='C' || c=='D' || c=='E' || c=='F';
	}

}
