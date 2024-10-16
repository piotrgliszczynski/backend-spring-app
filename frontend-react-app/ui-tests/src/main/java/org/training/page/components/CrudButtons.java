package org.training.page.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.page.base.BasePage;

public class CrudButtons extends BasePage {

  private final By deleteButton = By.id("btn-delete");
  private final By saveButton = By.id("btn-save");
  private final By cancelButton = By.id("btn-cancel");

  public CrudButtons(WebDriver driver) {
    super(driver);
  }

  public void clickDelete() {
    clickButton(deleteButton);
  }

  public void clickSave() {
    clickButton(saveButton);
  }

  public void clickCancel() {
    clickButton(cancelButton);
  }

  public void clickButton(By button) {
    wait.until(ExpectedConditions.elementToBeClickable(button)).click();
  }

  public String getDeleteButtonText() {
    return wait.until(
        ExpectedConditions.visibilityOfElementLocated(deleteButton)).getText();
  }

  public String getSaveButtonText() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(saveButton)).getText();
  }

  public String getCancelButtonText() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(cancelButton)).getText();
  }
}
