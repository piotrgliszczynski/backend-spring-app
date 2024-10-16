package org.training.steps;

import org.openqa.selenium.WebDriver;
import org.training.data.Customer;
import org.training.page.FormPage;
import org.training.steps.base.BaseSteps;

public class AddUpdateFormSteps extends BaseSteps {

  private final FormPage formPage;

  public AddUpdateFormSteps(WebDriver driver) {
    super(driver);
    formPage = new FormPage(driver);
  }

  public AddUpdateFormSteps openFormPage() {
    formPage.load();
    return this;
  }

  public AddUpdateFormSteps typeData(Customer customer) {
    typeData(customer.getName(), customer.getEmail(), customer.getPassword());
    return this;
  }

  public AddUpdateFormSteps typeData(String name, String email, String password) {
    formPage.getAddUpdateForm().typeName(name);
    formPage.getAddUpdateForm().typeEmail(email);
    formPage.getAddUpdateForm().typePassword(password);
    return this;
  }

  public boolean isFormVisible() {
    if (!formPage.getAddUpdateForm().getAddUpdateFormTitle().equals("Add")
        && !formPage.getAddUpdateForm().getAddUpdateFormTitle().equals("Update")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getNameLabel().equals("Name:")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getNamePlaceholder().equals("Customer Name")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getEmailLabel().equals("Email:")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getEmailPlaceholder().equals("name@company.com")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getPasswordLabel().equals("Pass:")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getPasswordPlaceholder().equals("password")) {
      return false;
    }
    if (!areButtonsVisible()) {
      return false;
    }
    return true;
  }

  public boolean areButtonsVisible() {
    if (!formPage.getAddUpdateForm().getCrudButtons().getDeleteButtonText().equals("Delete")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getCrudButtons().getSaveButtonText().equals("Save")) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getCrudButtons().getCancelButtonText().equals("Cancel")) {
      return false;
    }
    return true;
  }

  public boolean isFormInAddState() {
    return formPage.getAddUpdateForm().getAddUpdateFormTitle().equals("Add");
  }

  public boolean isFormInUpdateState() {
    return formPage.getAddUpdateForm().getAddUpdateFormTitle().equals("Update");
  }

  public boolean areFieldsEmpty() {
    if (!formPage.getAddUpdateForm().getNameValue().isEmpty()) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getEmailValue().isEmpty()) {
      return false;
    }
    if (!formPage.getAddUpdateForm().getPasswordValue().isEmpty()) {
      return false;
    }
    return true;
  }

  public boolean areFieldsFilled(Customer customer) {
    return formPage.getAddUpdateForm().getNameValue().equals(customer.getName())
        && formPage.getAddUpdateForm().getEmailValue().equals(customer.getEmail())
        && formPage.getAddUpdateForm().getPasswordValue().equals(customer.getPassword());
  }

  public boolean fieldsEqual(String name, String email, String password) {
    return formPage.getAddUpdateForm().getNameValue().equals(name)
        && formPage.getAddUpdateForm().getEmailValue().equals(email)
        && formPage.getAddUpdateForm().getPasswordValue().equals(password);
  }

  public void clickDelete() {
    formPage.getAddUpdateForm().getCrudButtons().clickDelete();
    formPage.getAddUpdateForm().handleAlert();
  }

  public void clickSave() {
    formPage.getAddUpdateForm().getCrudButtons().clickSave();
    formPage.getAddUpdateForm().handleAlert();
  }

  public void clickCancel() {
    formPage.getAddUpdateForm().getCrudButtons().clickCancel();
  }
}
