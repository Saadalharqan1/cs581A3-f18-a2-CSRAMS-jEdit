/*
 * StructureMatcher.java - Abstract interface for bracket matching, etc.
 * :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.textarea;

//{{{ Imports
import java.awt.*;
import org.gjt.sp.jedit.TextUtilities;
import org.gjt.sp.jedit.View;
//}}}

/**
 * An interface for matching parts of a source file's stucture. The default
 * implementation matches brackets. The XML plugin provides an implementation
 * for matching XML tags.
 *
 * @author Slava Pestov
 * @version $Id$
 * @since jEdit 4.2pre3
 */
public interface StructureMatcher
{
	//{{{ getMatch() method
	/**
	 * Returns the element matching the one at the given text area's
	 * caret position, or null.
	 * @since jEdit 4.2pre3
	 */
	Match getMatch(TextArea textArea);
	//}}}

	//{{{ selectMatch() method
	/**
	 * Selects from the caret to the matching structure element (if there is
	 * one, otherwise the behavior of this method is undefined).
	 * @since jEdit 4.2pre3
	 */
	void selectMatch(TextArea textArea);
	//}}}

	//{{{ BracketMatcher class
	static class BracketMatcher implements StructureMatcher
	{
		public Match getMatch(TextArea textArea)
		{
			int offset = textArea.getCaretPosition()
				- textArea.getLineStartOffset(
				textArea.getCaretLine());

			if(offset != 0)
			{
				int bracketOffset = TextUtilities.findMatchingBracket(
					textArea.getBuffer(),
					textArea.getCaretLine(),
					offset - 1);
				if(bracketOffset != -1)
				{
					int bracketLine = textArea
						.getLineOfOffset(
						bracketOffset);
					return new Match(this,
						bracketLine,
						bracketOffset,
						bracketLine,
						bracketOffset + 1);
				}
			}

			return null;
		}

		public void selectMatch(TextArea textArea)
		{
			textArea.selectToMatchingBracket();
		}
	} //}}}

	//{{{ Match class
	/**
	 * A structure match, denoted by a start and end position.
	 * @since jEdit 4.2pre3
	 */
	public static class Match
	{
		public StructureMatcher matcher;
		public int startLine;
		public int start;
		public int endLine;
		public int end;

		public Match() {}

		public Match(StructureMatcher matcher)
		{
			this.matcher = matcher;
		}

		public Match(StructureMatcher matcher, int startLine,
			int start, int endLine, int end)
		{
			this(matcher);
			this.startLine = startLine;
			this.start = start;
			this.endLine = endLine;
			this.end = end;
		}
		
		int[] getOffsets(int screenLine, Highlight highlight)
		{
			int x1, x2;
		
			int matchStartLine = highlight.textArea.getScreenLineOfOffset(
				start);
			int matchEndLine = highlight.textArea.getScreenLineOfOffset(
				end);
		
			if(matchStartLine == screenLine)
			{
				x1 = start;
			}
			else
			{
				x1 = highlight.textArea.getScreenLineStartOffset(
					screenLine);
			}
		
			if(matchEndLine == screenLine)
			{
				x2 = end;
			}
			else
			{
				x2 = highlight.textArea.getScreenLineEndOffset(
					screenLine) - 1;
			}
		
			return new int[] {
				highlight.textArea.offsetToXY(x1).x,
				highlight.textArea.offsetToXY(x2).x
			};
		}
	} //}}}

	
	
	//{{{ Highlight class
	/**
	 * Paints the structure match highlight.
	 */
	static class Highlight extends TextAreaExtension
	{
		Highlight(TextArea textArea)
		{
			this.textArea = textArea;
		}

		public void paintValidLine(Graphics2D gfx, int screenLine,
			int physicalLine, int start, int end, int y)
		{
			if(!textArea.getPainter().isStructureHighlightEnabled())
				return;

			Match match = textArea.getStructureMatch();
			if(match != null)
			{
				paintHighlight(gfx,screenLine,
					start,end,y,match);
			}
		}

		private void paintHighlight(Graphics gfx, int screenLine,
			int start, int end, int y,
			Match match)
		{
			if(!textArea.isStructureHighlightVisible())
				return;

			if(match.start >= end || match.end < start)
			{
				return;
			}

			int matchStartLine = textArea.getScreenLineOfOffset(
				match.start);
			int matchEndLine = textArea.getScreenLineOfOffset(
				match.end);

			int height = Math.min(
				textArea.getPainter().getLineHeight(), textArea.getPainter().getFontHeight());
			y += Math.max(textArea.getPainter().getLineExtraSpacing(), 0);

			int[] offsets = match.getOffsets(screenLine,Highlight.this);
			int x1 = offsets[0];
			int x2 = offsets[1];

			gfx.setColor(textArea.getPainter().getStructureHighlightColor());

			gfx.drawLine(x1,y,x1,y + height - 1);
			gfx.drawLine(x2,y,x2,y + height - 1);

			if(matchStartLine == screenLine || screenLine == 0)
				gfx.drawLine(x1,y,x2,y);
			else
			{
				offsets = match.getOffsets(screenLine - 1,Highlight.this);
				int prevX1 = offsets[0];
				int prevX2 = offsets[1];

				gfx.drawLine(Math.min(x1,prevX1),y,
					Math.max(x1,prevX1),y);
				gfx.drawLine(Math.min(x2,prevX2),y,
					Math.max(x2,prevX2),y);
			}

			if(matchEndLine == screenLine)
			{
				gfx.drawLine(x1,y + height - 1,
					     x2,y + height - 1);
			}
		}

		private TextArea textArea;
	} //}}}
}
