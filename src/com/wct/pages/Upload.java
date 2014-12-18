package com.wct.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;

import com.wct.dao.PersonDao;
import com.wct.entities.Person;
import com.wct.panels.UploadForm;
import com.wct.provider.PersonDataProvider;
import com.wct.template.TemplatePage;

public class Upload extends TemplatePage {

	private ModalWindow modal;

	
	private WebMarkupContainer containerTable;
	
	private int count = 0;
	
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Upload(PageParameters params) {
		super(params);
		modal = new ModalWindow("modal");
		add(modal);
		add(new AjaxLink<String>("importBtn") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				// modal.setContent(new Uplo)
				modal.setContent(new UploadForm(modal.getContentId()){

					@Override
					public void callbackMethod(AjaxRequestTarget target) {
						target.add(containerTable);
						System.out.println("------CALL TO ABSTRACT IN MMODAL WINDO -------");
					}
					
				} );
				modal.setTitle("Uploader le fichier");
				modal.setWidthUnit("px");
				modal.setHeightUnit("px");
				modal.setInitialHeight(500);
				modal.setInitialWidth(500);
				modal.show(target);
			}

		});
		containerTable = new WebMarkupContainer("tableContainer");
		containerTable.setOutputMarkupId(true);
		containerTable.add(createTable());
		add(containerTable);
		final Label numberLabel = new Label("number",new PropertyModel<Integer>(this, "count"));
		numberLabel.setOutputMarkupId(true);
		numberLabel.add(new AjaxSelfUpdatingTimerBehavior(Duration.ONE_SECOND));
		add(numberLabel);

		add(new AjaxLink<String>("count") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.add(numberLabel);
				System.out.println(count);
				count++;
			}
			
		});
		
		
	}

	private DataView<Person> createTable() {
		PersonDataProvider personsProvider = new PersonDataProvider();
		DataView<Person> personsDataView = new DataView<Person>("items", personsProvider) {

			@Override
			protected void populateItem(Item<Person> item) {
				Person p = item.getModelObject();
				item.add(new Label("id", new PropertyModel<Integer>(p, "id")));
				item.add(new Label("firstName", new PropertyModel<String>(p, "firstName")));
				item.add(new Label("lastName", new PropertyModel<String>(p, "lastName")));
				item.add(new Label("age", new PropertyModel<Integer>(p, "age")));

			}
		};
		personsDataView.setOutputMarkupId(true);
		return personsDataView;
	}
}
