package com.bstek.dorado.console.syslog;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SystemLog extends AbstractWork {
	
	private Monitor sysoutMonitor;
	private Monitor syserrMonitor;
	
	private static SystemLog instance = new SystemLog();
	
	public static SystemLog getInstance() {
		return instance;
	}
	
	private SystemLog() {}
	
	@Override
	protected void doStartWork() {
		sysoutMonitor = new SysoutMonitor();
		syserrMonitor = new SyserrMonitor();
		
		PrintStream newSysout = new PrintStream(sysoutMonitor);
		PrintStream newSyserr = new PrintStream(syserrMonitor);
		
		System.setOut(newSysout);
		System.setErr(newSyserr);
	}

	@Override
	protected void doStopWork() {
		PrintStream oldSysout = sysoutMonitor.getReal();
		PrintStream oldSyserr = syserrMonitor.getReal();
		
		System.setOut(oldSysout);
		System.setErr(oldSyserr);
		
		sysoutMonitor = null;
		syserrMonitor = null;
	}
	
	/**
	 * 对于System.out的监控器
	 *
	 */
	class SysoutMonitor extends Monitor {
		public SysoutMonitor() {
			super(System.out);
		}
		
		@Override
		protected LogLine createObject(String str) {
			LogLine s = new LogLine(str, "sysout");
			return s;
		}
	}
	
	/**
	 * 对于System.err的监控器
	 *
	 */
	class SyserrMonitor extends Monitor {
		public SyserrMonitor() {
			super(System.err);
		}
		
		@Override
		protected LogLine createObject(String str) {
			LogLine s = new LogLine(str, "syserr");
			return s;
		}
	}
	
	abstract class Monitor extends OutputStream {

		private PrintStream real;
		private List<Byte> buffer;
		
		private char lineChar;
		
		public Monitor(PrintStream printStream) {
			this.real = printStream;
			this.buffer = this.createByteBuffer ();
			String s = System.getProperty("line.separator");
			lineChar = s.charAt(s.length() -1);
		}
		
		PrintStream getReal() {
			return real;
		}
		
		@Override
		synchronized
		public void write(int b) throws IOException {
			buffer.add(Integer.valueOf(b).byteValue());
			
			if (b == lineChar) {
				String line = makeString();
				LogLine obj = createObject(line);
				Event event = new Event(Monitor.this, obj);
				try {
					getPublisher().publish(event);
				} catch (Throwable t) {
					onError(t, line);
				} finally {
					real.print(line);
				}
			}
		}
		
		protected String makeString() {
			byte[] bs = new byte[buffer.size()];
			for (int i=0; i<buffer.size(); i++) {
				bs[i] = buffer.get(i);
			}
			String str = new String(bs);
			buffer = this.createByteBuffer ();
			return str;
		}

		protected List<Byte> createByteBuffer () {
			return new ArrayList<Byte>(200);
		}
		
		protected abstract LogLine createObject(String str);
		
		protected void onError(Throwable t, String str) {
			
		}
		
	}

}
