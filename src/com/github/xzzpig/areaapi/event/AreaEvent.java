package com.github.xzzpig.areaapi.event;

import com.github.xzzpig.areaapi.Area;
import com.github.xzzpig.pigutils.annoiation.NotNull;
import com.github.xzzpig.pigutils.event.Event;

public abstract class AreaEvent extends Event{
	protected Area area;
	
	protected AreaEvent(@NotNull Area area) {
		this.area = area;
	}
	
	public Area getArea(){
		return area;
	}
}
