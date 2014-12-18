package com.wct.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.wct.feedback.FeedbackPanelMsg;
import com.wct.panels.RecursivePanel;
import com.wct.template.TemplatePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

public class Test extends TemplatePage {


	private final FileUploadField file;
    private final TextField<String> text;


	public Test(PageParameters params) {
		super(params);


        // create a feedback panel
        final FeedbackPanelMsg feedback = new FeedbackPanelMsg("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        // create the form
        final Form<?> form = new Form<Void>("form")
        {
            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            protected void onSubmit()
            {
                // display uploaded info
                info("Text: " + text.getModelObject());
                FileUpload upload = file.getFileUpload();
                if (upload == null)
                {
                    info("No file uploaded");
                }
                else
                {
                	info(" Content Type : " + upload.getContentType());
                    info("File-Name: " + upload.getClientFileName() + " File-Size: " +
                        Bytes.bytes(upload.getSize()).toString());
                }
            }
        };
        form.setMaxSize(Bytes.megabytes(1));
        add(form);

        // create a textfield to demo non-file content
        form.add(text = new TextField<String>("text", new Model<String>()));

        // create the file upload field
        form.add(file = new FileUploadField("file"));
        
        file.setRequired(true);
        
        file.add(new TestValidator()); 
        
        form.add(new Label("max", new AbstractReadOnlyModel<String>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject()
            {
                return form.getMaxSize().toString();
            }
        }));

        form.add(new UploadProgressBar("progress", form, file));

        
        // create the ajax button used to submit the form
        form.add(new AjaxButton("ajaxSubmit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                info("This request was processed using AJAX");

                FileUpload fileUpload = file.getFileUpload();
                
                
                info(file.getFileUpload().getClientFileName());
                // ajax-update the feedback panel
                target.add(feedback);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                // update feedback to display errors
                target.add(feedback);
            }

        });
        

		// create a list with sublists
		List<Object> l1 = new ArrayList<Object>();
		l1.add("test 1.1");
		l1.add("test 1.2");
		List<Object> l2 = new ArrayList<Object>();
		l2.add("test 2.1");
		l2.add("test 2.2");
		l2.add("test 2.3");
		List<Object> l3 = new ArrayList<Object>();
		l3.add("test 3.1");
		l3.add("test 3.2");
		
		l2.add(l3);
		l2.add("test 2.4");
		l1.add(l2);
		l1.add("test 1.3");
		List<String> l4 = new ArrayList<String>();
		l4.add("test 4.1");
		l4.add("test 4.2");
		l3.add(l4);
		// construct the panel
		add(new RecursivePanel("recursive", l1));
	}
	
	
	public class TestValidator implements IValidator<List<FileUpload>> {

		@Override
		public void validate(IValidatable<List<FileUpload>> validatable) {
			// TODO Auto-generated method stub
			List<FileUpload> list = validatable.getValue();
			for (FileUpload f:list) {
				error(f.getClientFileName());
			}
			
		}
		
	}
	

	
}
