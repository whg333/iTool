package com.why.tool.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AIOClientTest {

	private static final long TIMEOUT = TimeUnit.SECONDS.toMillis(3);
	
	private String[] ports;
	private String ip;
	
	public static void main(String[] args) throws IOException {
		new AIOClientTest().start(args);
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
		long start = System.currentTimeMillis();
		Selector selector = Selector.open();
		
		Map<SocketChannel, InetSocketAddress> channelMap = new HashMap<SocketChannel, InetSocketAddress>();
		Map<InetSocketAddress, Integer> taskMap = new HashMap<InetSocketAddress, Integer>(ports.length);
		for(int i=0;i<ports.length;i++){
			SocketChannel clientChannel = SocketChannel.open();
			clientChannel.configureBlocking(false);
			
			clientChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
			//clientChannel.register(selector, SelectionKey.OP_CONNECT);
			
			int port = Integer.parseInt(ports[i]);
			InetSocketAddress address = new InetSocketAddress(ip, port);
			channelMap.put(clientChannel, address);
			taskMap.put(address, i+1);
			System.out.println("taskMap.put=["+address+" = "+taskMap.get(address)+"]");
			
//			clientChannel.connect(address);
			
			System.out.println("begin connect to "+address+"...");
			if(!clientChannel.connect(address)){
				while (!clientChannel.finishConnect()) {  
					System.out.println("connecting to "+address+"..."); 
		        }
			}
			System.out.println("success connected to "+address+"...");
		}
		
		int[] readSum = new int[3];
		while(!taskMap.isEmpty()){
			selector.select(TIMEOUT);
			
			Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();
		    while (keyIt.hasNext()) {
		    	SelectionKey key = keyIt.next();
		    	keyIt.remove();
		    	
		    	//当时卡在这里了，因为认为上面clientChannel.connect(address)了以后，就连接上了，但其实不一定
		    	//既然不一定的话，那这个名为key.isConnectable的害我以为是得到了这一步，代表可以连接了
		    	//才调用clientChannel.connect(address)去连呢，结果发现报错说正阻塞在连接中
		    	//java.nio.channels.ConnectionPendingException，那key.isConnectable什么时候用？
//		    	if(key.isConnectable()){
//		    		SocketChannel channel = (SocketChannel)key.channel();
//		    		System.out.println(channelMap.get(channel));
//		    		//channel.connect(channelMap.get(channel));
//		    		System.out.println("channel.isConnected()="+channel.isConnected());
//		    		System.out.println("channel.isBlocking()="+channel.isBlocking());
//		    		channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
//		    	}
		    	
		    	if(key.isReadable()){
		    		SocketChannel channel = (SocketChannel)key.channel();
		    		//注意这里调用的是远程连接的Socket地址getRemoteSocketAddress()，而不是getInetAddress
		    		SocketAddress address = channel.socket().getRemoteSocketAddress();
		    		//System.out.printf("\nTask %d connected to %s", taskMap.get(address), address);
		    		ByteBuffer readBuf = (ByteBuffer)key.attachment();
		    		int taskNum = taskMap.get(address);
		    		int readSize;
		    		while((readSize=channel.read(readBuf)) > 0){
		    			System.out.printf("\nTask %d: got %d bytes of poetry from %s", taskNum, readSize, address);
		    			readSum[taskNum-1] += readSize;
		    			
		    			//这里你不消耗掉readBuf的话，就会被填满，导致后续服务器端发送过来的都没地方read接受了
		    			//这个问题是发现服务器端一直不停的在传送直到完毕后，但这里的read最后read到4个字节后就不往下read了
		    			//后来想到是readBuf的大小为1024，修改为4000后就可以了，因为服务器传送文件大小在3000左右
		    			//最后才想到为什么1024不继续往下read呢？原来是满了呀。。。
		    			//所以下面这里加了clear，即代表业务中对接受到的消息做处理/消耗
		    			readBuf.clear();
		    		}
		    		if(readSize < 0){
		                channel.socket().close();
		                System.out.printf("\nTask %d finished", taskMap.get(address));
		                taskMap.remove(address);
		    		}
		    	}
		    	
		    }
		}
		
		for(int i=0;i<readSum.length;i++){
			System.out.printf("\nTask %d: %d bytes of poetry", i + 1, readSum[i]);
		}
		
		System.out.printf("\nGot %d poems in %ss", ports.length, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
	}
	
}
