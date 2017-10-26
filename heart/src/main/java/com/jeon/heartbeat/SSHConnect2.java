package com.jeon.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnect2 {
	
	/**

     * 서버에 연결하는 호스트 이름 (or IP 주소)

     */

    private String hostname;

    

    /**

     * 해당 서버에 있는 사용자 이름

     */

    private String username;

    

    /**

     * 해당 서버에 있는 사용자의 암호

     */

    private String password;

    
    
    private JSch jsch;
    private Session session;


    
 

    /**

     * SSHAgent 객체 생성

     * 

     * @param hostname

     * @param username

     * @param password
    

     */

    public SSHConnect2( String hostname, String username, String password )

    {

        this.hostname = hostname;

        this.username = username;

        this.password = password;

    }
	
    public boolean connect() throws Exception

    {
    	jsch=new JSch();
        
		try

        {

        	
    		
    		session = jsch.getSession(username, hostname, 22);
    		
    		session.setPassword(password);
    		Properties config = new Properties();
    		config.put("StrictHostKeyChecking", "no");
    		session.setConfig(config);
    		session.connect();

        }

        catch( Exception e )

        {

            throw new Exception( "호스트에 연결하는 동안 예외가 발생하였습니다 : " 
            + hostname + ", Exception=" + e.getMessage(), e ); 

        }
        
        return session.isConnected();

    }
	
    
    
    public String executeCommand( String command ) throws Exception 

    {

    	StringBuilder sb=null;
    	ChannelExec channel=(ChannelExec) session.openChannel("exec");
    	System.out.println(command);
    	try
        {
        	
    		channel.setPty(true);
    		channel.setCommand(command);
    		BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
    		channel.connect();
    		
    		sb = new StringBuilder();
    		String msg=null;
    		while((msg=in.readLine())!=null){
    		  System.out.println(msg);
    		  sb.append( msg + "\n" );
    		}

    		System.out.println("=====================================");
    		System.out.println( "ExitCode : " + channel.getExitStatus() );
    		System.out.println("=====================================");
    		
    		channel.disconnect();
    		
        }catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			channel.disconnect();
			logout();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			channel.disconnect();
			logout();
		}
        
        return sb.toString();
    }
    
    
    public void logout()
    {
    	session.disconnect();
    }
    
	
	public static void main( String[] args )
	{
		

		
		
		try{
		JSch jsch2=new JSch();
		Session session;
		
		session = jsch2.getSession("haadmin", "192.168.23.130", 22);
		
		session.setPassword("admin123");
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		
		
		ChannelExec channel=(ChannelExec) session.openChannel("exec");
		channel.setPty(true);
		///opt/haadmin/monitor os
		channel.setCommand("sudo /opt/test 192.168.23.131 mariadb admin123 /opt/data /opt/data");
		//channel.setInputStream(null);
		BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
		channel.connect();
		
		
		String msg=null;
		while((msg=in.readLine())!=null){
		  System.out.println(msg);
		}

		System.out.println( "ExitCode : " + channel.getExitStatus() );
		
		channel.disconnect();
		session.disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
