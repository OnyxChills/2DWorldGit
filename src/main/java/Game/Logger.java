package Game;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;



public class Logger {
	private String file;
	public PrintWriter p;

	public Logger(String file){
		this.file = file;
	}
	
	public void startLogging(){
		try{
			p = new PrintWriter(new File(this.file + ".log"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeToLog(String a){
		try{
			p.println(/*getCallerClassName() + " " + */a);
		} catch (Exception e) {
		}
	}
	
	public void writeToConsole(String a){
			System.out.println(getCallerClassName() + " " + a);
	}
	
	public static String getCallerClassName() { 
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Logger.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
     }
	
	public static String getCallerCallerClassName() { 
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Logger.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
     }
	
	public void writeToBoth(String a){
		writeToConsole(a);
		writeToLog(a);
	}
	
	public void endLogging(){
		try {
			p.close();
		} catch (Exception e) {
		}
	}
	
	public long time(Method m, Object obj, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		long now, then, delta;
		then = System.nanoTime();
		
		m.invoke(obj, args);
		
		now = System.nanoTime();
		delta = now - then;
				
		return delta;
	}
	
	public void writeHeader(){
		this.writeToLog("===========================");
		this.writeToLog("======World of Debug=======");
		this.writeToLog("===========================");
		this.writeToLog("");
	}
}
