package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class SignUpPage {

    public SignUpPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(name = "email")
    public WebElement email;

    @FindBy(name = "name")
    public WebElement name;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(name = "password_confirmation")
    public WebElement password_confirmation;

    @FindBy(xpath = "//button[.='Sign Up']")
    public WebElement signUpButton;

    @FindBy(id = "toast-container")
    public WebElement successMessage;

}
