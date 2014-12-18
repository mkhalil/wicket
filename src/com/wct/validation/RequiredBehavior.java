package com.wct.validation;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class RequiredBehavior extends Behavior {

	@Override
	public void onComponentTag(Component component, ComponentTag tag)
	{
		if (component.hasErrorMessage()) {
			String className = tag.getAttribute("class");
			tag.put("class", className + " error");
		}
	}
}
