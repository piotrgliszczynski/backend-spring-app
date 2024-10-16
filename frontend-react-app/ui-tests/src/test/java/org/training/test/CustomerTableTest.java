package org.training.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.training.data.Customer;
import org.training.data.providers.CustomerDataProvider;
import org.training.steps.CustomerTableSteps;
import org.training.test.base.BaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTableTest extends BaseTest {

  private CustomerTableSteps customerTableSteps;

  @Test
  void shouldContainHeaderRow_In_CustomerTable_When_NavigatingToMainPage() {
    // Given
    String expectedNameHeader = "Name";
    String expectedEmailHeader = "Email";
    String expectedPasswordHeader = "Pass";
    customerTableSteps.openHomePage();

    // When
    String actualNameHeader = customerTableSteps.getNameHeader();
    String actualEmailHeader = customerTableSteps.getEmailHeader();
    String actualPasswordHeader = customerTableSteps.getPasswordHeader();

    // Then
    assertAll(
        () -> assertEquals(expectedNameHeader, actualNameHeader),
        () -> assertEquals(expectedEmailHeader, actualEmailHeader),
        () -> assertEquals(expectedPasswordHeader, actualPasswordHeader)
    );
  }

  @ParameterizedTest
  @ArgumentsSource(CustomerDataProvider.class)
  void customerShouldBeVisible_When_NavigatingToMainPage(Customer customer) {
    // Given
    customerTableSteps.openHomePage();

    // When
    List<Customer> customers = customerTableSteps.getAllCustomers();

    // Then
    assertAll(() -> assertEquals(3, customers.size()),
        () -> assertTrue(customers.contains(customer)));
  }

  @Test
  void customerRowShouldBeClickable_When_NavigateToMainPAge() {
    // Given
    customerTableSteps.openHomePage();

    // When
    boolean isClickable = customerTableSteps.isRowClickable();

    // Then
    assertTrue(isClickable);
  }

  @Test
  void customerRowShouldBeBold_When_CustomerRowIsClicked() {
    // Given
    customerTableSteps.openHomePage();

    // When
    customerTableSteps.clickCustomer();
    boolean isSelected = customerTableSteps.isSelected();

    // Then
    assertTrue(isSelected);
  }

  @Test
  void customerRowShouldBeNormal_When_CustomerRowIsDeselected() {
    // Given
    customerTableSteps.openHomePage();

    // When
    customerTableSteps
        .clickCustomer()
        .clickCustomer();
    boolean isSelected = customerTableSteps.isSelected();

    // Then
    assertFalse(isSelected);
  }

  @Test
  void newCustomerRowShouldBeSelected_When_ClickingNewRow() {
    // Given
    customerTableSteps.openHomePage()
        .clickFirstCustomer();

    // When
    customerTableSteps.clickSecondCustomer();
    boolean isSecondSelected = customerTableSteps.secondIsSelected();

    // Then
    assertTrue(isSecondSelected);
  }

  @Test
  void oldCustomerRowShouldBeDeselected_When_ClickingNewRow() {
    // Given
    customerTableSteps.openHomePage()
        .clickFirstCustomer();

    // When
    customerTableSteps.clickSecondCustomer();
    boolean isFirstSelected = customerTableSteps.firstIsSelected();

    // Then
    assertFalse(isFirstSelected);
  }

  @Override
  public void prepareSteps() {
    customerTableSteps = new CustomerTableSteps(getDriver());
  }
}
