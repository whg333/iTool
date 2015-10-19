package com.why.tool.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.why.tool.string.StringUtil;

public class BIOServerTest {

	private int port;
	private String ip;
	private String filePath;
	private int delay;
	private int bytes;

	private File file;
	private int readSize;
	private byte[] readBuf;

	public static void main(String[] args) throws IOException, InterruptedException {
		new BIOServerTest().start(args);
	}

	public void start(String[] args) throws IOException, InterruptedException {
		parseArgs(args);
		server();
	}

	private void parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("port", true, "The port to listen on. Default to a random available port.");
		options.addOption("ip", true, "The interface to listen on. Default is localhost.");
		options.addOption("file", true, "The file to send");
		options.addOption("delay", true, "The number of seconds between sending bytes.");
		options.addOption("bytes", true, "The number of bytes to send at a time.");

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("NIOTest", options);

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String portStr = cmd.getOptionValue("port");
		if (StringUtil.isEmpty(portStr) || !StringUtil.isNumber(portStr)) {
			errorExit("port param is need!");
		}
		port = Integer.parseInt(portStr);

		filePath = cmd.getOptionValue("file");
		if (StringUtil.isEmpty(filePath)) {
			errorExit("file param is need!");
		}
		file = new File(filePath);
		if (!file.exists()) {
			errorExit("file not exists!file=" + file.getAbsolutePath());
		}

		ip = cmd.hasOption("ip") ? cmd.getOptionValue("ip") : "localhost";
		delay = cmd.hasOption("delay") ? Integer.parseInt(cmd.getOptionValue("delay")) : 100;
		bytes = cmd.hasOption("bytes") ? Integer.parseInt(cmd.getOptionValue("bytes")) : 10;

		readBuf = new byte[bytes];
	}

	private void errorExit(String errorMsg) {
		System.out.println(errorMsg);
		System.exit(0);
	}

	private void server() throws IOException, InterruptedException {
		ServerSocket servSock = new ServerSocket();
		SocketAddress address = new InetSocketAddress(ip, port);
		servSock.bind(address, 5);
		System.out.println("Server listen at "+address);
		while (true) {
			Socket clientSocket = servSock.accept();
			System.out.println("accept a client request...");
			SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
			System.out.println("Handling client at "+clientAddress);

			InputStream in = new FileInputStream(file);
			OutputStream out = clientSocket.getOutputStream();
			while ((readSize = in.read(readBuf)) != -1) {
				try{
					out.write(readBuf, 0, readSize);
					System.out.println("Sending "+readSize+" bytes");
				}catch(SocketException se){
					se.printStackTrace();
					System.out.println("socket exception occupt on client "+clientAddress);
					close(in, out, clientSocket);
				}
				Thread.sleep(delay);
			}
			close(in, out, clientSocket);
		}
	}
	
	private void close(InputStream in, OutputStream out, Socket clientSocket) throws IOException{
		System.out.println("finished handle request...");
		in.close();
		out.close();
		clientSocket.close();
		System.out.println("closed...");
	}

}
