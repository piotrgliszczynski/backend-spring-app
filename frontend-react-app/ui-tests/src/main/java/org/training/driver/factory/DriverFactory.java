package org.training.driver.factory;

import org.training.driver.ChromeDriverManager;
import org.training.driver.DriverManager;
import org.training.driver.type.DriverType;

public class DriverFactory {

  private DriverFactory() {
  }

  public static DriverManager getManager(DriverType driverType) {
    return switch (driverType) {
      case CHROME -> new ChromeDriverManager();
    };
  }
}
