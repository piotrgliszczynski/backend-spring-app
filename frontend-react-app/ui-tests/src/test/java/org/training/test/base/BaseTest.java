package org.training.test.base;

import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.training.driver.factory.DriverFactory;
import org.training.driver.type.DriverType;
import org.training.test.config.ScreenShotter;

@Getter
@ExtendWith(ScreenShotter.class)
public abstract class BaseTest {

  private WebDriver driver;

  @BeforeEach
  void startDriver() {
    driver = DriverFactory.getManager(DriverType.CHROME).createDriver();
    prepareSteps();
    prepareScreenshotter();
  }

  public void prepareScreenshotter() {
    ScreenShotter.setDriver(getDriver());
  }

  public abstract void prepareSteps();

  @AfterEach
  void quitDriver() {

    driver.quit();
  }
}
