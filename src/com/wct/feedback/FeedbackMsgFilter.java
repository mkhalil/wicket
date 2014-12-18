package com.wct.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

public class FeedbackMsgFilter implements IFeedbackMessageFilter {

	private final int errorLevel;
	
	/*** error level ****/
	public FeedbackMsgFilter(int errorLevel) {
		this.errorLevel = errorLevel;
	}
	
	@Override
	public boolean accept(FeedbackMessage message) {
		return (this.errorLevel == message.getLevel());
	}


}
