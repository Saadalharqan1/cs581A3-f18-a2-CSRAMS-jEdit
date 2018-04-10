/**
 * 
 */
package org.gjt.sp.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import org.gjt.sp.jedit.EditPane;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.ScrollLayout;
import org.gjt.sp.jedit.textarea.TextArea;

/**
 * @author Ahmar Aftab
 *
 */
public class ViewUtils {
	
	//Change Request#2: added a hashmap DS to store the values of horizontal and Vertical scroll bars
		public static Map<String,Box> textAreaConfigCache = new HashMap<String,Box>();
		
	//Change Request#2 for performing toggle operations of scroll Bars
		public void toggleScrollBars(EditPane editPane)
		{
		
			TextArea textArea = editPane.getTextArea();
			
			
			
			if (!jEdit.getBooleanProperty("view.scroll.bars.enabled")) {
		
				textAreaConfigCache.put("verticalBox",textArea.getVerticalBox());
				textAreaConfigCache.put("horizontalBox",textArea.getHorizontalBox());
				textArea.remove(textArea.getVerticalBox());	
				textArea.remove(textArea.getHorizontalBox());
				jEdit.setBooleanProperty("view.scroll.bars.enabled", true);
			}else if (jEdit.getBooleanProperty("view.scroll.bars.enabled")){
				
				if (textAreaConfigCache.get("verticalBox") != null) {
					textArea.add(ScrollLayout.RIGHT,textAreaConfigCache.get("verticalBox"));	
				}
				
				if (textAreaConfigCache.get("horizontalBox") != null) {
					textArea.add(ScrollLayout.BOTTOM,textAreaConfigCache.get("horizontalBox"));	
				}
				jEdit.setBooleanProperty("view.scroll.bars.enabled", false);
				
			}
				
			textArea.revalidate();
			// so you can keep typing in your editpane afterwards...
			textArea.requestFocus();
			
			//
			//toggleFullScreen();
		}
}
