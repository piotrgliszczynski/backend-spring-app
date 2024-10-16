package org.training.page.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.page.base.BasePage;

public class CustomerListTitle extends BasePage {

  private final By customerListTitle = By.id("customer-list-title");

  public CustomerListTitle(WebDriver driver) {
    super(driver);
  }

  public String getTitle() {
    return wait
        .until(ExpectedConditions.visibilityOfElementLocated(customerListTitle))
        .getText();
  }
}
