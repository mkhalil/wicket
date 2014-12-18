package com.wct.applications;

import java.nio.channels.SeekableByteChannel;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IApplicationSettings;
import org.apache.wicket.settings.def.ApplicationSettings;
import org.apache.wicket.settings.def.ResourceSettings;

import com.wct.pages.Upload;

public class CrudApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return Upload.class;
	}
	
	public CrudApplication() {
		
		// this.mountPage("test.html", Test.class);
	}
	
	@Override
	protected void init() {
		super.init();
		getResourceSettings().setLocalizer(new BaseLocalizer());
		System.out.print("ffff Localier --------------");
		
	}
	

	
}
