package org.training.steps;

import org.openqa.selenium.WebDriver;
import org.training.page.HomePage;
import org.training.steps.base.BaseSteps;

public class CustomerListTitleSteps extends BaseSteps {

  private final HomePage homePage;

  public CustomerListTitleSteps(WebDriver driver) {
    super(driver);
    homePage = new HomePage(driver);
  }

  public CustomerListTitleSteps openHomePage() {
    homePage.load();
    return this;
  }

  public String getCustomerListTitle() {
    return homePage.getCustomerListTitle().getTitle();
  }
}
