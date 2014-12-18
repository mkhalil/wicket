package com.wct.pages;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.wct.dao.CustomerDAO;
import com.wct.dao.DAOException;
import com.wct.entities.Customer;
import com.wct.panels.PagingNavigatorBootstrap;
import com.wct.panels.PayLoadTest;
import com.wct.provider.CustomerDataProvider;
import com.wct.provider.CustomerSortableDataProvider;
import com.wct.template.TemplatePage;

public class Index extends TemplatePage {

	private DataView<Customer> dataView;
	private PagingNavigator navigator;
	private Link<String> btnShowPoopUp;
	
	public Index(PageParameters params) {
		super(params);
		this.createTable();

	}

	private void createTable() {
		CustomerSortableDataProvider customerSortableDP = new CustomerSortableDataProvider();
		add(new OrderByBorder<String>("orderById", "id", customerSortableDP) {
			@Override 
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		
		add(new OrderByBorder<String>("orderByFirstName", "firstName", customerSortableDP){
			@Override 
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		add(new OrderByBorder<String>("orderByLastName", "lastName", customerSortableDP) {
			@Override 
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});
		
		add(new OrderByBorder<String>("orderByStreet", "street", customerSortableDP) {
			@Override 
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		dataView = new DataView<Customer>("customers", customerSortableDP) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
			}
			
			@Override
			protected void populateItem(final Item<Customer> item) {
				item.add(new Label("id", item.getModelObject().getId()));
				item.add(new Label("firstName", item.getModelObject().getFirstName()));
				item.add(new Label("lastName", item.getModelObject().getLastName()));
				item.add(new Label("street",  item.getModelObject().getStreet()));
				item.add(new Link<String>("edit"){

					@Override
					public void onClick() {
					//	Parameters
						//setResponsePage(Read.class, parameters)	
						PageParameters params = new PageParameters();
						params.add("id", item.getModelObject().getId());
						this.setResponsePage(Read.class, params);
					}});

				item.add(new Link<Customer>("delete", item.getModel()){

					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						System.out.println("To Delete : " + this.getModelObject());
						try {
							CustomerDAO customerDAO = new CustomerDAO();
							customerDAO.delete(this.getModelObject());
							feedBackPanelMsg.success("Deleted!!!!!!");
						} catch (DAOException e) {
							// TODO Auto-generated catch block
							feedBackPanelMsg.error("Error!!!!!!" + e.getMessage());
							e.printStackTrace();
						}
						
					}
					
				});
			}
		};
		dataView.setItemsPerPage(5);
		add(dataView);
		navigator = new PagingNavigatorBootstrap("navigator", dataView);
		add( navigator);

	}

	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		
		if (event.getPayload() instanceof PayLoadTest) {
			System.out.println("------------------PayLoad Event Test ---------------------");
			System.out.println("----Payload Event --- " + WebSession.get().getAttribute("searchword"));
			System.out.println("-------------Evenet Handler ----------------------");
		}
	}
	
}
