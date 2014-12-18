package com.wct.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;


public class MenuItem extends WebMarkupContainer {

	public MenuItem(String id) {
		super(id);
	}
	
	
	public MenuItem(String id, Class <? extends Page> linkTo,  Class <? extends Page> currentPage) {
		super(id);
		if (currentPage.equals(linkTo)) {
			add(new AttributeModifier("class", "active"));
		} 
		BookmarkablePageLink<String> link = new BookmarkablePageLink<String>("linkMenu", linkTo);
		add(link);
	}
}
