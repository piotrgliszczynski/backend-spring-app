package org.training.test;

import org.junit.jupiter.api.Test;
import org.training.steps.CustomerListTitleSteps;
import org.training.test.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerListTitleTest extends BaseTest {

  private final static String CUSTOMER_LIST_TITLE = "Customer List";

  private CustomerListTitleSteps customerListTitleSteps;

  @Test
  void shouldDisplayCorrectCustomerListTitle_When_NavigateToMainPage() {
    // Given
    customerListTitleSteps.openHomePage();

    // When
    String actualTitle = customerListTitleSteps.getCustomerListTitle();

    // Then
    assertEquals(CUSTOMER_LIST_TITLE, actualTitle);
  }

  @Override
  public void prepareSteps() {
    customerListTitleSteps = new CustomerListTitleSteps(getDriver());
  }
}
