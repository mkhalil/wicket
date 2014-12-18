package com.wct.applications;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import com.wct.pages.Index;

public class RepeaterApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return Index.class;
	}
}
