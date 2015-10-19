package com.why.tool.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class BIOClientTest {

	private String[] ports;
	private String ip;
	
	public static void main(String[] args) throws IOException {
		new BIOClientTest().start(args);
	}
	
	public void start(String[] args) throws IOException{
		parseArgs(args);
		getContent();
	}
	
	private void parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("port", true, "The port to connect.");
		options.addOption("ip", true, "The interface to connect. Default is localhost.");

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("NIOClientTest", options);

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!cmd.hasOption("port")) {
			errorExit("port param is need!");
		}
		ports = cmd.getOptionValues("port");
		System.out.println("ports=" + Arrays.toString(ports));

		ip = cmd.hasOption("ip") ? cmd.getOptionValue("ip") : "localhost";
	}
	
	private void errorExit(String errorMsg) {
		System.out.println(errorMsg);
		System.exit(0);
	}
	
	public void getContent() throws IOException{
		long elapsed = 0;
		for(int i=0;i<ports.length;i++){
			elapsed += getContent(i);
		}
		System.out.printf("\nGot %d poems in %ss", ports.length, TimeUnit.MILLISECONDS.toSeconds(elapsed));
	}
	
	public long getContent(int i) throws IOException{
		long start = System.currentTimeMillis();
		int port = Integer.parseInt(ports[i]);
		InetSocketAddress address = new InetSocketAddress(ip, port);
		System.out.printf("\nTask %d: get poetry from: %s", i+1, address);
		Socket socket = new Socket(address.getAddress(), address.getPort());
		int readSize, readSum = 0;
		byte[] readBuf = new byte[1024];
		InputStream in = socket.getInputStream();
		while((readSize=in.read(readBuf)) != -1){
			readSum += readSize;
		}
		
		in.close();
		socket.close();
		
		long time = System.currentTimeMillis() - start;
		System.out.printf("\nTask %d: got %d bytes of poetry from %s in %ss", i+1, readSum, address, TimeUnit.MILLISECONDS.toSeconds(time));
		return time;
	}
	
}
