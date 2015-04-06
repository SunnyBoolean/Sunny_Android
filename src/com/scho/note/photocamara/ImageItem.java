package com.scho.note.photocamara;

import java.io.Serializable;

/**
 * �?张图片的实体�?
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	/** 图片id*/
	public String imageId;
	/** 该图片所在相册的路径*/
	public String thumbnailPath;
	/** 该图片的路径*/
	public String imagePath;
	/** 是否选择*/
	public boolean isSelected = false;
}
