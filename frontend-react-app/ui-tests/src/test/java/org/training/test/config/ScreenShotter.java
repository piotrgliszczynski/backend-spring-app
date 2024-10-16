package org.training.test.config;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenShotter implements AfterTestExecutionCallback {

  private static WebDriver driver;

  public static void setDriver(WebDriver driver) {
    ScreenShotter.driver = driver;
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    if (context.getExecutionException().isPresent()) {

      String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
      String classFolder = context.getRequiredTestClass().getSimpleName();
      String baseFileName = context.getRequiredTestMethod().getName()
          + LocalDateTime.now().format(DateTimeFormatter.ofPattern("-yyMMdd-HHmmss"));

      File targetFile = new File("screenshots" + File.separator
          + browserName + File.separator
          + classFolder + File.separator
          + baseFileName + ".png");
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      Files.createDirectories(targetFile.toPath().getParent());
      Files.copy(scrFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }
}
