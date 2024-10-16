package org.training.page.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.page.base.BasePage;

public class Header extends BasePage {

  public final By formLink = By.xpath("//a[contains(@href, 'customer-form')]");

  public Header(WebDriver driver) {
    super(driver);
  }

  public void clickFormLink() {
    wait.until(ExpectedConditions.elementToBeClickable(formLink)).click();
  }
}
