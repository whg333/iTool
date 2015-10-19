package com.why.tool.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ASCIITest {

	public static void main(String[] args) throws IOException {
		File targetFile = new File("C:/Documents and Settings/Administrator/桌面/ASCIITest_1.txt");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<10;i++){
			sb.append(i+"\r\n");
		}
		sb.append("0123456789\r\n");
		FileUtils.write(targetFile, sb, "UTF-8");
	}
	
}
