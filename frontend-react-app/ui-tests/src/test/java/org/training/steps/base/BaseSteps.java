package org.training.steps.base;

import org.openqa.selenium.WebDriver;

public abstract class BaseSteps {

  protected final WebDriver driver;

  public BaseSteps(WebDriver driver) {
    this.driver = driver;
  }
}
