package org.noip.naszdomek.SBackup;

import org.noip.naszdomek.SBackup.config.Config;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class Main 
{
	private Config config;
	private App app;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
               
        Main main = new Main();
        
        main.config = context.getBean(Config.class);
        main.config.setCmdParams(args);
        
        main.app = context.getBean(App.class);
        main.app.run();
        
        context.close();
        
      
    }
}
