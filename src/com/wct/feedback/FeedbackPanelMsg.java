package com.wct.feedback;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.PropertyModel;


public class FeedbackPanelMsg extends FeedbackPanel {


	public FeedbackPanelMsg(String id) {
		super(id);		
	}
	
	
	public FeedbackPanelMsg(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
	}
	
	/**
	 * Gets the css class for the given message.
	 * 
	 * @param message
	 *            the message
	 * @return the css class; by default, this returns feedbackPanel + the message level, eg
	 *         'feedbackPanelERROR', but you can override this method to provide your own
	 */
	@Override
	protected String getCSSClass(final FeedbackMessage message)
	{		
		return "feedbackPanel" + message.getLevelAsString();
	}
	
 	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();		
		this.getButtonToggle().setVisible(false);
		int error = 0, info = 0, warning = 0, success = 0;
		List<FeedbackMessage> feedbackMessages = this.getCurrentMessages();
		System.out.println(feedbackMessages.size());
		for (FeedbackMessage feedbackMessage : feedbackMessages) {
			if (feedbackMessage.getLevel() == FeedbackMessage.ERROR) {
				error++;
			}
			if (feedbackMessage.getLevel() == FeedbackMessage.INFO) {
				info++;
			}
			if (feedbackMessage.getLevel() == FeedbackMessage.WARNING) {
				warning++;
			}
			if (feedbackMessage.getLevel() == FeedbackMessage.SUCCESS) {
				success++;
			}
		}
		StringBuffer strBuffer = new StringBuffer();
		if (error > 0) {
			strBuffer.append(error);
			strBuffer.append(" : Error (s)");
			strBuffer.append(";");
			this.getMessagesContainer().add(new AttributeModifier("class", "alert alert-dismissable alert-danger"));
			this.getButtonClose().setVisible(false);
			this.getButtonToggle().setVisible(true);
		}
		if (warning > 0) {
			strBuffer.append(warning);
			strBuffer.append(" : Warning (s)");
			strBuffer.append(";");
			if (error == 0) {
				this.getMessagesContainer().add(new AttributeModifier("class", "alert alert-dismissable alert-warning"));
			}
		}
		if (info > 0) {
			strBuffer.append(info);
			strBuffer.append(" : Info (s)");
			strBuffer.append(";");
			if (error == 0 && warning == 0) {
				this.getMessagesContainer().add(new AttributeModifier("class", "alert alert-dismissable alert-info"));
			}
		}
		if (success > 0) {
			strBuffer.append(success);
			strBuffer.append(" : Success (s)");
			strBuffer.append(";");
			if (error == 0 && warning == 0 && info == 0) {
				this.getMessagesContainer().add(new AttributeModifier("class", "alert alert-dismissable alert-success"));
			}
		}
		if (strBuffer.length() > 0) {
			strBuffer.deleteCharAt(strBuffer.length() - 1);
		}
		this.setTitle(strBuffer.toString());
		this.setSortingComparator(new ComporatorFeedBack());
	}
	
	

}
