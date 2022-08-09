package blxt.qjava.qlog;
     
import java.text.SimpleDateFormat; 

/**
 * 控制台日志输出.
 * @author ZhangJieLei
 *
 */
public class Log {
	  
	final static String red    = "\u001b[00;31m";    //$NON-NLS-1$        
	final static String green  = "\u001b[00;32m";    //$NON-NLS-1$        
	final static String yellow = "\u001b[00;33m";    //$NON-NLS-1$        
	final static String purple = "\u001b[00;34m";    //$NON-NLS-1$        
	final static String pink   = "\u001b[00;35m";    //$NON-NLS-1$        
	final static String cyan   = "\u001b[00;36m";    //$NON-NLS-1$        
	final static String end    = "\u001b[00m";       //$NON-NLS-1$      

	final static String DEBUG = " [DEBUG] ";
	final static String INFO  = " [INFO ] ";
	final static String WARN  = " [WARN ] ";
	final static String ERROR = " [ERROR] ";
	
   
	// 日志分隔符
	final static String space = "-"; //$NON-NLS-1$
	
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS"); //$NON-NLS-1$
 
	// 日志tag
	String tag = "";

	public Log( ) {

	}


	public Log(String tag) {
		this.tag = tag;
	}


	/**
	 * 输出info日志.
	 * @param fromt
	 * @param msg
	 */
	public void d(final String fromt, final Object ...msg) {
		i(MessageFormatter.arrayFormat(fromt, msg).getMessage());
	}

	/**
	 * 输出info日志.
	 * @param msg
	 */
	public synchronized void d(final String msg) {
		String tag = MessageFormatter.arrayFormat("{} {} {} {} ", getTag(this.tag, DEBUG)).getMessage() + msg;   //$NON-NLS-1$
		println(tag);
	}

	/**
	 * 输出info日志.
	 * @param fromt
	 * @param msg
	 */
	public void i(final String fromt, final Object ...msg) {  
		i(MessageFormatter.arrayFormat(fromt, msg).getMessage());
	}
	
	/**
	 * 输出info日志.
	 * @param msg
	 */
	public synchronized void i(final String msg) { 
		String tag = MessageFormatter.arrayFormat("{} {} {} {} ", getTag(this.tag, INFO)).getMessage() + msg;   //$NON-NLS-1$
		println(tag);
	}
	
	/**
	 * 输出worn日志.
	 * @param fromt
	 * @param msg
	 */
	public synchronized void w(final String fromt, final Object ...msg) {  
		w(MessageFormatter.arrayFormat(fromt, msg).getMessage());
	}
	
	/**
	 * 输出worn日志.
	 * @param msg
	 */
	public synchronized void w(final String msg) {  
		String tag = MessageFormatter.arrayFormat("{} {} {} {} ", getTag(this.tag, WARN)).getMessage() + msg;   //$NON-NLS-1$
		println(tag);
	}
	
	/**
	 * 输出error日志.
	 * @param fromt
	 * @param msg
	 */
	public synchronized void e(final String fromt, final Object ...msg) {   
		e(MessageFormatter.arrayFormat(fromt, msg).getMessage()); 
	}
	
	/**
	 * 输出error日志.
	 * @param msg
	 */
	public synchronized void e(final String msg) { 
		String tag = MessageFormatter.arrayFormat("{} {} {} {} ", getTag(this.tag, ERROR)).getMessage() + msg;   //$NON-NLS-1$
		println(tag);
	}
	
	
	/**
	 * 输出信息到控制台.
	 * @param msg 消息
	 */
	public void println(final String msg) { 
		System.out.print(msg);
	}
	
	
	/**
	 * 获取info等级日志信息.
	 * @return
	 */
	private static Object[] getTag(String tag, String live) {
		Object[] datas = new Object[4];
		datas[0] = tag;
		// 时间  
		datas[1] = simpleDateFormat.format(System.currentTimeMillis());
		datas[2] = green + live + end;
		// 分隔符
		datas[3] = space;
		return datas;
	}
	  
}
