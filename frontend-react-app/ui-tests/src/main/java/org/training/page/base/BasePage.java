package org.training.page.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

  protected final WebDriver driver;
  protected final WebDriverWait wait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  protected void load(String endpoint) {
    driver.get(endpoint);
  }
}
