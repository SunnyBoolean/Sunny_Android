package com.scho.note.photocamara;

import java.io.Serializable;

/**
 * ä¸?å¼ å¾ççå®ä½ç±?
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	/** å¾çid*/
	public String imageId;
	/** è¯¥å¾çæå¨ç¸åçè·¯å¾*/
	public String thumbnailPath;
	/** è¯¥å¾ççè·¯å¾*/
	public String imagePath;
	/** æ¯å¦éæ©*/
	public boolean isSelected = false;
}
