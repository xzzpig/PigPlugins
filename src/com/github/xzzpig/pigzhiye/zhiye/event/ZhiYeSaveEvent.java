package com.github.xzzpig.pigzhiye.zhiye.event;

import java.io.File;

import com.github.xzzpig.pigzhiye.zhiye.ZhiYe;

public class ZhiYeSaveEvent extends ZhiYeEvent{

	private final File file;
	
	public ZhiYeSaveEvent(ZhiYe zy,File f) {
		super(zy);
		file = f;
	}
	
	public File getFile(){
		return file;
	}

}
