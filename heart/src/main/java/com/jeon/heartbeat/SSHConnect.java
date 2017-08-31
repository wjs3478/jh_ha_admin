package com.jeon.heartbeat;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class SSHConnect {
	
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

    

    /**

     * 서버 연결 커넥션 객체

     */

    private Connection connection;
 

    /**

     * SSHAgent 객체 생성

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

     * 서버에 연결

     * 

     * @return        연결 성공 시 true, 그 외에는 false

     */

    public boolean connect() throws Exception

    {

        try

        {

            // 서버 연결 객체 생성

            connection = new Connection( hostname );

            connection.connect();

            

            // 인증

            boolean result = connection.authenticateWithPassword( username,    

                                   password );

            System.out.println( "연결 성공 여부 : " + result );

            return result;

        }

        catch( Exception e )

        {

            throw new Exception( "호스트에 연결하는 동안 예외가 발생하였습니다 : " 
            + hostname + ", Exception=" + e.getMessage(), e ); 

        }

    }

    

    /**

     * 지정된 명령을 실행하고 서버에서 응답을 반환한다.

     *  

     * @param command        command 실행

     * @return               서버에서 반환하는 응답 (or null)

     */

    public String executeCommand( String command ) throws Exception 

    {

        try

        {

            // 세션 생성

            Session session = connection.openSession();

            

            // command 실행

            session.execCommand( command );

            

            // 결과 확인

            StringBuilder sb = new StringBuilder();

            InputStream stdout = new StreamGobbler( session.getStdout() );

            BufferedReader br = new BufferedReader(new 

                                             InputStreamReader(stdout));

            String line = br.readLine();

            while( line != null )

            {

                sb.append( line + "\n" );

                line = br.readLine();

            }



            // DEBUG : exit 코드 덤프

            System.out.println( "ExitCode : " + session.getExitStatus() );


            
            // 세션 종료

            session.close();

            

            // 호출자에게 결과를 반환

            return sb.toString();

        }

        catch( Exception e )

        {

            throw new Exception( "다음 명령을 실행하는 동안 예외가 발생하였습니다. : "+ command + ". Exception = " + e.getMessage(), e );

        }

    }



    /**

     * 서버에서 로그 아웃

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

            throw new Exception( "SSH 연결을 종료하는 동안 예외가 발생했습니다. : " + e.getMessage(), e );

        }

    }

    

    /**

     * 기본 인증이 완료되면 true를 반환하고 그렇지 않을 경우 false

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

            SSHConnect sshAgent = new SSHConnect( "192.168.112.2", "root", 

                                                               "a1004zgp!" );

            if( sshAgent.connect() ) 

            {

                String diskInfo = sshAgent.executeCommand( "ls" );

                System.out.println( "디스크 정보 : " + diskInfo );

                

                String processInfo = sshAgent.executeCommand( "/home/test/monitor ip" );

                System.out.println( "프로세스 정보 : " + processInfo );

                

                // 로그아웃

                sshAgent.logout();

            }

        }

        catch( Exception e )

        {

            e.printStackTrace();

        }

    }



	
	
}
