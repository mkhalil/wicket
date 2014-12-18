package com.wct.pages;


import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.wct.entities.Customer;
import com.wct.template.TemplatePage;

public class UserPage extends TemplatePage {

	private Form<UserPage> userForm;
	private TextField<String> firstName;
	private TextField<String> lastName;
	private TextField<String> street;
	
	public UserPage() {
		
		userForm = new Form<UserPage>("userForm");
		/*firstName = new TextField<String>("firstName", new PropertyModel<String>(customer, "firstName"));
		lastName = new TextField<String>("lastName", new PropertyModel<String>(customer, "lastName"));
		street = new TextField<String>("street", new PropertyModel<String>(customer, "street"));*/

		add(userForm);
	}
}
