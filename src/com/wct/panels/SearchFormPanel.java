package com.wct.panels;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;

public class SearchFormPanel extends Panel {

	private String searchword;
	
	public String getSearchword() {
		return searchword;
	}

	public void setSearchword(String searchword) {
		this.searchword = searchword;
	}

	public SearchFormPanel(String id) {
		super(id);
	
		Form<String> searchForm = new Form<String>("searchForm") {
			
			@Override
			public void onSubmit() {
				WebSession.get().setAttribute("searchword", getSearchword());
				send(getPage(), Broadcast.BREADTH, new PayLoadTest());
			}
			
		};
		
		searchForm.add(new TextField<String>("searchword", new PropertyModel<String>(this, "searchword")));
		add(searchForm);
	}

	
}
