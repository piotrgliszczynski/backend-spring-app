package org.training.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.page.base.BasePage;
import org.training.page.components.AddUpdateForm;
import org.training.page.components.Header;
import org.training.utils.config.TestConfig;

@Getter
public class FormPage extends BasePage {


  private final AddUpdateForm addUpdateForm;
  private final Header header;

  public FormPage(WebDriver driver) {
    super(driver);
    addUpdateForm = new AddUpdateForm(driver);
    header = new Header(driver);
  }

  public void load() {
    load(TestConfig.baseUrl + TestConfig.formPage);
    wait.until(ExpectedConditions.titleContains("Customers App"));
  }
}
