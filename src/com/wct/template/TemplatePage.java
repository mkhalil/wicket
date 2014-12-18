package com.wct.template;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.wct.feedback.ComporatorFeedBack;
import com.wct.feedback.FeedbackPanelMsg;
import com.wct.feedback.PanelFeedback;
import com.wct.pages.Create;
import com.wct.pages.Index;
import com.wct.pages.Test;
import com.wct.panels.MenuItem;
import com.wct.panels.SearchFormPanel;

public abstract class TemplatePage extends WebPage {

	public FeedbackPanelMsg feedBackPanelMsg;
	
	public PanelFeedback feedbackPanel;

	
	public TemplatePage(final PageParameters parameters) {
		super(parameters);
		this.addElements();
		if (parameters.get("successMsg") != null && !parameters.get("successMsg").isEmpty()) {
			feedBackPanelMsg.success(parameters.get("successMsg").toString());
		}
	}

	private void addElements() {
		/**
		 * Header Form
		 */
		add(new SearchFormPanel("searchPanel"));
		System.out.println("Page Class = " + this.getPageClass());
		feedBackPanelMsg = new FeedbackPanelMsg("feedBackMsg");	
		feedbackPanel = new PanelFeedback("feedBackMsg");
		// System.out.println(feedBackPanelMsg.getFeedbackMessages().size());
		// feedBackPanelMsg.setSo
		add(new MenuItem("listLink", Index.class, this.getPageClass()));
		add(new MenuItem("createLink", Create.class, this.getPageClass()));
		add(new MenuItem("testLink", Test.class, this.getPageClass()));
		System.out.println("-----------TEST--------------");
		add(feedbackPanel);
	
			
	}
	
	public TemplatePage() {
		super();
		this.addElements();
	}
	
}

