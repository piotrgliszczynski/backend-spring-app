package org.training.steps;

import org.openqa.selenium.WebDriver;
import org.training.data.Customer;
import org.training.page.HomePage;
import org.training.steps.base.BaseSteps;

import java.util.List;

public class CustomerTableSteps extends BaseSteps {

  private final HomePage homePage;

  public CustomerTableSteps(WebDriver driver) {
    super(driver);
    homePage = new HomePage(driver);
  }

  public CustomerTableSteps openHomePage() {
    homePage.load();
    return this;
  }

  public String getNameHeader() {
    return homePage.getCustomerTable().getCustomerTableNameHeader();
  }

  public String getEmailHeader() {
    return homePage.getCustomerTable().getCustomerTableEmailHeader();
  }

  public String getPasswordHeader() {
    return homePage.getCustomerTable().getCustomerTablePasswordHeader();
  }

  public List<Customer> getAllCustomers() {
    return homePage.getCustomerTable().getAllCustomers();
  }

  public boolean isRowClickable() {
    return homePage.getCustomerTable().isCustomerClickable();
  }

  public CustomerTableSteps clickCustomer() {
    homePage.getCustomerTable().clickCustomer();
    return this;
  }

  public CustomerTableSteps clickFirstCustomer() {
    homePage.getCustomerTable().clickCustomer(1);
    return this;
  }

  public CustomerTableSteps clickSecondCustomer() {
    homePage.getCustomerTable().clickCustomer(2);
    return this;
  }

  public CustomerTableSteps clickOnCustomer(Customer customer) {
    homePage.getCustomerTable().clickCustomer(customer);
    return this;
  }

  public boolean isSelected() {
    return homePage.getCustomerTable().getRowClass().contains("selected");
  }

  public boolean firstIsSelected() {
    return homePage.getCustomerTable().getRowClass(1).contains("selected");
  }

  public boolean secondIsSelected() {
    return homePage.getCustomerTable().getRowClass(2).contains("selected");
  }

  public boolean isCustomerSelected(Customer customer) {
    return homePage.getCustomerTable().getRowClass(customer).contains("selected");
  }

  public AddUpdateFormSteps clickFormLink() {
    homePage.getHeader().clickFormLink();
    return new AddUpdateFormSteps(driver);
  }
}
