package com.wct.panels;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class MenuLangItem extends WebMarkupContainer {

	public MenuLangItem(String id, IModel<String> langTo) {
		super(id);
		// TODO Auto-generated constructor stub
		add(new Link<String>("flagLink", langTo) {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				// getSession().setLocale(new Locale(this.getModelObject()));
				// getSession().setLocale(new Locale(this.getModelObject()));
				System.out.println("-----" + this.getModelObject());
			}
		});
		
	}

}
