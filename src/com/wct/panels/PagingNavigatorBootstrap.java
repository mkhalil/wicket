package com.wct.panels;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

public class PagingNavigatorBootstrap extends PagingNavigator {

	public PagingNavigatorBootstrap(String id, IPageable pageable) {
		super(id, pageable);
	}

}
