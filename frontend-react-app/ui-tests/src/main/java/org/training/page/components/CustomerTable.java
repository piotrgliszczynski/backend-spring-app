package org.training.page.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.training.data.Customer;
import org.training.page.base.BasePage;

import java.util.List;

public class CustomerTable extends BasePage {

  private final By customerTable = By.id("customer-table");
  private final By customerRow = By.cssSelector("#customer-table > tbody > tr");

  public CustomerTable(WebDriver driver) {
    super(driver);
  }

  public String getCustomerTableNameHeader() {
    WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(customerTable));
    return wait.until(
        ExpectedConditions.visibilityOf(
            table.findElement(By.cssSelector("thead > tr > th:nth-child(1)")))
    ).getText();
  }

  public String getCustomerTableEmailHeader() {
    WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(customerTable));
    return wait.until(
        ExpectedConditions.visibilityOf(
            table.findElement(By.cssSelector("thead > tr > th:nth-child(2)")))
    ).getText();
  }

  public String getCustomerTablePasswordHeader() {
    WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(customerTable));
    return wait.until(
        ExpectedConditions.visibilityOf(
            table.findElement(By.cssSelector("thead > tr > th:nth-child(3)")))
    ).getText();
  }

  public List<Customer> getAllCustomers() {
    WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(customerTable));

    wait.until(ExpectedConditions.visibilityOfElementLocated(customerRow));
    List<WebElement> rows = table.findElements(By.cssSelector("tbody > tr"));
    return rows.stream()
        .map(row -> row.findElements(By.tagName("td")))
        .map(columns -> new Customer(
            columns.get(0).getText(),
            columns.get(1).getText(),
            columns.get(2).getText())
        )
        .toList();
  }

  public void clickCustomer() {
    isCustomerClickable();
    driver.findElement(customerRow).click();
  }

  public void clickCustomer(int row) {
    By customerRow = By.cssSelector(String.format("#customer-table > tbody > tr:nth-child(%d)", row));
    isCustomerClickable();
    wait.until(ExpectedConditions.elementToBeClickable(customerRow)).click();
  }

  public void clickCustomer(Customer customer) {
    By customerCell = By.xpath(String.format("//td[text()='%s']", customer.getName()));
    wait.until(ExpectedConditions.elementToBeClickable(customerCell)).click();
  }

  public boolean isCustomerClickable() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(customerTable));
    wait.until(ExpectedConditions.elementToBeClickable(customerRow));
    return true;
  }

  public String getRowClass() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(customerRow)).getAttribute("class");
  }

  public String getRowClass(int row) {
    By customerRow = By.cssSelector(String.format("#customer-table > tbody > tr:nth-child(%d)", row));
    return wait.until(ExpectedConditions.visibilityOfElementLocated(customerRow)).getAttribute("class");
  }

  public String getRowClass(Customer customer) {
    By customerRow = By.xpath(String.format("//tr[td[text()='%s']]", customer.getName()));
    return wait.until(ExpectedConditions.visibilityOfElementLocated(customerRow)).getAttribute("class");
  }
}
