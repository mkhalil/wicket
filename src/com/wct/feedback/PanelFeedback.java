package com.wct.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.Panel;


public class PanelFeedback extends Panel {

	public PanelFeedback(String id) {
		super(id);
		this.add(new FeedbackPanelMsg("feedbackPanelERROR", new FeedbackMsgFilter(FeedbackMessage.ERROR)));
		this.add(new FeedbackPanelMsg("feedbackPanelWARNING", new FeedbackMsgFilter(FeedbackMessage.WARNING)));
		this.add(new FeedbackPanelMsg("feedbackPanelSUCCESS", new FeedbackMsgFilter(FeedbackMessage.SUCCESS)));
		this.add(new FeedbackPanelMsg("feedbackPanelINFO", new FeedbackMsgFilter(FeedbackMessage.INFO)));

	}

	
}
