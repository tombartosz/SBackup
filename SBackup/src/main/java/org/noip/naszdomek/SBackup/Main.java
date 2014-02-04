package org.noip.naszdomek.SBackup;

import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main 
{
	@Autowired
	private Config config;
	
	@Autowired
	private App app;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
              
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
               
        Main main = context.getBean(Main.class);
        
        main.config.setCmdParams(args);
        main.app.run();
        
        context.close();
        
      
    }
}
