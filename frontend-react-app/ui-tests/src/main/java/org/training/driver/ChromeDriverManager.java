package org.training.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager implements DriverManager {

  @Override
  public WebDriver createDriver() {
    WebDriverManager.chromedriver().cachePath("drivers").setup();
    WebDriver driver = new ChromeDriver(prepareOptions());
    driver.manage().window().maximize();
    return driver;
  }

  private ChromeOptions prepareOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-search-engine-choice-screen");
    return options;
  }
}
