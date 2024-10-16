package org.training.page.components;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.page.base.BasePage;

@Getter
public class AddUpdateForm extends BasePage {

  private final By addUpdateFormTitle = By.id("add-update-form-title");

  private final By nameLabel = By.cssSelector("label[for='name']");
  private final By nameInput = By.id("name");
  private final By emailLabel = By.cssSelector("label[for='email']");
  private final By emailInput = By.id("email");
  private final By passwordLabel = By.cssSelector("label[for='password']");
  private final By passwordInput = By.id("password");

  private final CrudButtons crudButtons;

  public AddUpdateForm(WebDriver driver) {
    super(driver);
    crudButtons = new CrudButtons(driver);
  }

  public void typeName(String name) {
    typeData(nameInput, name);
  }

  public void typeEmail(String email) {
    typeData(emailInput, email);
  }

  public void typePassword(String password) {
    typeData(passwordInput, password);
  }

  private void typeData(By element, String data) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(element)).sendKeys(data);
  }

  public void handleAlert() {
    wait.until(ExpectedConditions.alertIsPresent()).accept();
  }

  public String getAddUpdateFormTitle() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(addUpdateFormTitle)).getText();
  }

  public String getNameLabel() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(nameLabel)).getText();
  }

  public String getNamePlaceholder() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).getAttribute("placeholder");
  }

  public String getNameValue() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).getAttribute("value");
  }

  public String getEmailLabel() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(emailLabel)).getText();
  }

  public String getEmailPlaceholder() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).getAttribute("placeholder");
  }

  public String getEmailValue() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).getAttribute("value");
  }

  public String getPasswordLabel() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordLabel)).getText();
  }

  public String getPasswordPlaceholder() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).getAttribute("placeholder");
  }

  public String getPasswordValue() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).getAttribute("value");
  }
}
