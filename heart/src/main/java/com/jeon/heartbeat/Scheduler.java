package com.jeon.heartbeat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	
	@Scheduled(cron = "0 50 17 * * *")
	public void cronTest1(){
		System.out.println("�ȳ�");
	}
	
	@Scheduled(cron = "*/10 * * * * *")
	public void cronTest4(){
		System.out.println("�ȳ� �� ������ �������� �� �� �� �ϴ� ��� �ʹ�");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron = "*/10 * * * * *")
	public void cronTest5(){
		System.out.println("232323232331");
		
	}
	
	
	@Scheduled(cron = "0 50 14 * * *")
	public void cronTest3(){
		System.out.println("�ȳ�");
	}

	
	@Scheduled(cron = "0 51 17 * * *")
	public void cronTest2(){
		System.out.println("�ȳ�");
	}

/*	@Scheduled(fixedRate = 5000)
	public void simpleexe() {
		System.out.println("-------------------------------");
	}*/
}