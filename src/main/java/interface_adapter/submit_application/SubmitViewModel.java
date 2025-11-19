package interface_adapter.submit_application;

import interface_adapter.ViewModel;

public class SubmitViewModel extends ViewModel<SubmitState> {

    public static final String TITLE_LABEL = "Start Your Application";
    public static final String USERNAME_LABEL = "Username";
    public static final String FIRSTNAME_LABEL = "First Name";
    public static final String LASTNAME_LABEL = "Last Name";
    public static final String AGE_LABEL = "Enter your age";
    public static final String OCCUPATION_LABEL = "Enter your occupation";
    public static final String ADDRESS_LABEL = "Enter your address";
    public static final String HOME_LABEL = "Describe your home environment";
    public static final String REASON_LABEL = "Reason(s) for wanting to adopt a pet";
    public static final String EXP_LABEL = "Describe your previous experience keeping a pet or pets; " +
            "if none, write None.";
    public static final String AVAILABILITY_LABEL = "How much time can you spend with the pet?";
    public static final String EMAIL_LABEL = "Enter your email";
    public static final String TEL_LABEL = "Enter your phone number";

    public static final String SUBMIT_BUTTON_LABEL = "Submit";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public SubmitViewModel() {
        super("submit");
        setState(new SubmitState());
    }

}
