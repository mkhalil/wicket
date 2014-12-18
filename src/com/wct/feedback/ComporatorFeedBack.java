package com.wct.feedback;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.wicket.feedback.FeedbackMessage;

public class ComporatorFeedBack implements Comparator<FeedbackMessage>, Serializable {

	@Override
	public int compare(FeedbackMessage arg0, FeedbackMessage arg1) {
		return (arg1.getLevel() - arg0.getLevel());
	}

}
