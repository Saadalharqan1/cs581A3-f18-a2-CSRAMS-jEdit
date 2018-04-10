package org.gjt.sp.jedit.buffer;


import java.util.List;
import java.util.Vector;

import org.gjt.sp.jedit.buffer.JEditBuffer.Listener;
import org.gjt.sp.util.Log;

public class JEditBufferUtils {
	private final List<Listener> bufferListeners;

	{
		bufferListeners = new Vector<Listener>();
	}
	/**
	* Removes a buffer change listener.
	* @param listener  The listener
	* @since  jEdit 4.3pre3
	*/
	public void removeBufferListener(BufferListener listener) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			if (bufferListeners.get(i).listener == listener) {
				bufferListeners.remove(i);
				return;
			}
		}
	}

	/**
	* @return  an array of registered buffer change listeners.
	* @since  jEdit 4.3pre3
	*/
	public BufferListener[] getBufferListeners() {
		BufferListener[] returnValue = new BufferListener[bufferListeners.size()];
		for (int i = 0; i < returnValue.length; i++) {
			returnValue[i] = bufferListeners.get(i).listener;
		}
		return returnValue;
	}

	public BufferListener getListener(int index) {
		return bufferListeners.get(index).listener;
	}

	public void fireFoldLevelChanged(int start, int end, JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.foldLevelChanged(jEditBuffer, start, end);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void fireContentInserted(int startLine, int offset, int numLines, int length, JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.contentInserted(jEditBuffer, startLine, offset, numLines, length);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void fireContentRemoved(int startLine, int offset, int numLines, int length, JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.contentRemoved(jEditBuffer, startLine, offset, numLines, length);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void firePreContentInserted(int startLine, int offset, int numLines, int length, JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.preContentInserted(jEditBuffer, startLine, offset, numLines, length);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void firePreContentRemoved(int startLine, int offset, int numLines, int length, JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.preContentRemoved(jEditBuffer, startLine, offset, numLines, length);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void fireTransactionComplete(JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.transactionComplete(jEditBuffer);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void fireFoldHandlerChanged(JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.foldHandlerChanged(jEditBuffer);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	public void fireBufferLoaded(JEditBuffer jEditBuffer) {
		for (int i = 0; i < bufferListeners.size(); i++) {
			BufferListener listener = getListener(i);
			try {
				listener.bufferLoaded(jEditBuffer);
			} catch (Throwable t) {
				Log.log(Log.ERROR, jEditBuffer, "Exception while sending buffer event to " + listener + " :");
				Log.log(Log.ERROR, jEditBuffer, t);
			}
		}
	}

	/**
	* Adds a buffer change listener.
	* @param listener  The listener
	* @param priority  Listeners with HIGH_PRIORITY get the event before listeners with NORMAL_PRIORITY
	* @since  jEdit 4.3pre3
	*/
	public void addBufferListener(BufferListener listener, int priority) {
		Listener l = new JEditBuffer.Listener(listener, priority);
		for (int i = 0; i < bufferListeners.size(); i++) {
			Listener _l = bufferListeners.get(i);
			if (_l.priority < priority) {
				bufferListeners.add(i, l);
				return;
			}
		}
		bufferListeners.add(l);
	}
}