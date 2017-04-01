package com.github.xzzpig.pigzhiye.zhiye.event;

import com.github.xzzpig.pigapi.event.Event;
import com.github.xzzpig.pigzhiye.zhiye.ZhiYe;

public class ZhiYeEvent extends Event {
	private final ZhiYe zy;

	public ZhiYeEvent(ZhiYe zy) {
		this.zy = zy;
	}

	public ZhiYe getZhiYe() {
		return zy;
	}
}
