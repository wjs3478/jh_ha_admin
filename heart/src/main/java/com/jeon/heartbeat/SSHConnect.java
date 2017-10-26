package com.jeon.heartbeat;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class SSHConnect {
	
	/**

     * ������ �����ϴ� ȣ��Ʈ �̸� (or IP �ּ�)

     */

    private String hostname;

    

    /**

     * �ش� ������ �ִ� ����� �̸�

     */

    private String username;

    

    /**

     * �ش� ������ �ִ� ������� ��ȣ

     */

    private String password;

    

    /**

     * ���� ���� Ŀ�ؼ� ��ü

     */

    private Connection connection;
 

    /**

     * SSHAgent ��ü ����

     * 

     * @param hostname

     * @param username

     * @param password

     */

    public SSHConnect( String hostname, String username, String password )

    {

        this.hostname = hostname;

        this.username = username;

        this.password = password;

    }

    
    /**

     * ������ ����

     * 

     * @return        ���� ���� �� true, �� �ܿ��� false

     */

    public boolean connect() throws Exception

    {

        try

        {

            // ���� ���� ��ü ����

            connection = new Connection( hostname );

            connection.connect();

            

            // ����

            boolean result = connection.authenticateWithPassword( username,    

                                   password );

            System.out.println( "���� ���� ���� : " + result );

            return result;

        }

        catch( Exception e )

        {

            throw new Exception( "ȣ��Ʈ�� �����ϴ� ���� ���ܰ� �߻��Ͽ����ϴ� : " 
            + hostname + ", Exception=" + e.getMessage(), e ); 

        }

    }

    

    /**

     * ������ ����� �����ϰ� �������� ������ ��ȯ�Ѵ�.

     *  

     * @param command        command ����

     * @return               �������� ��ȯ�ϴ� ���� (or null)

     */

    public String executeCommand( String command ) throws Exception 

    {

        try

        {

            // ���� ����

            Session session = connection.openSession();
            //session.startShell();
            

            // command ����

            session.execCommand( command );

            System.out.println(command);
            
            //session.execCommand("cat /opt/haadmin.log");

            // ��� Ȯ��

            StringBuilder sb = new StringBuilder();

            InputStream stdout = new StreamGobbler( session.getStdout() );
            

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            String line = br.readLine();
            

            while( line != null )

            {

                sb.append( line + "\n" );

                line = br.readLine();
                
                System.out.println("�ù�"+line);

            }



            // DEBUG : exit �ڵ� ����

            System.out.println( "ExitCode : " + session.getExitStatus() );


            
            // ���� ����

            session.close();

            

            // ȣ���ڿ��� ����� ��ȯ
            //System.out.println(sb.toString());
            
            return sb.toString();

        }

        catch( Exception e )

        {

            throw new Exception( "���� ����� �����ϴ� ���� ���ܰ� �߻��Ͽ����ϴ�. : "+ command + ". Exception = " + e.getMessage(), e );

        }

    }



    /**

     * �������� �α� �ƿ�

     * @throws SSHException

     */

    public void logout() throws Exception

    {

        try

        {

            connection.close();

        }

        catch( Exception e )

        {

            throw new Exception( "SSH ������ �����ϴ� ���� ���ܰ� �߻��߽��ϴ�. : " + e.getMessage(), e );

        }

    }

    

    /**

     * �⺻ ������ �Ϸ�Ǹ� true�� ��ȯ�ϰ� �׷��� ���� ��� false

     * @return

     */

    public boolean isAuthenticationComplete()

    {

        return connection.isAuthenticationComplete();

    }

    

    public static void main( String[] args ) 

    {

        try

        {

            SSHConnect sshAgent = new SSHConnect( "192.168.23.130", "haadmin", 

                                                               "admin123" );

            if( sshAgent.connect() ) 

            {

                //String diskInfo = sshAgent.executeCommand( "ls" );

                //System.out.println( "��ũ ���� : " + diskInfo );

                
            	
            	
                //String processInfo = sshAgent.executeCommand( "/home/test/monitor ip" );

            	String pe=sshAgent.executeCommand("/opt/haadmin/monitor ip");
            	//sshAgent.executeCommand("cat /opt/haadmin/haadmin.log");
                System.out.println( "���μ��� ���� : " + pe );

                //System.out.println(sshAgent.executeCommand("ps -ef | grep tomcat"));
                //sshAgent.executeCommand("su -");
                //sshAgent.executeCommand("stat!@");

                String ps=sshAgent.executeCommand("/opt/haadmin/monitor os");
                System.out.println(ps);
                // �α׾ƿ�

                sshAgent.logout();

            }

        }

        catch( Exception e )

        {

            e.printStackTrace();

        }

    }



	
	
}
