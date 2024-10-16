package org.training.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.training.api.actions.CustomersApi;
import org.training.data.Customer;
import org.training.data.providers.CustomerDataProvider;
import org.training.exception.RestApiException;
import org.training.steps.AddUpdateFormSteps;
import org.training.steps.CustomerTableSteps;
import org.training.test.base.BaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CrudButtonTest extends BaseTest {

  private final CustomersApi customersApi = new CustomersApi();
  private AddUpdateFormSteps addUpdateFormSteps;
  private CustomerTableSteps customerTableSteps;

  private Customer createdCustomer;

  @AfterEach
  void deleteAllCreatedCustomers() {
    if (createdCustomer != null) {
      try {
        customersApi.deleteCustomer(createdCustomer);
      } catch (RestApiException e) {
        System.out.println("Record don't exist in the db already!");
      }
      createdCustomer = null;
    }
  }

  @Test
  void shouldDeleteRecord_AfterClick_DeleteButton() throws RestApiException {
    // Given
    Customer customer = new Customer("test", "test@test.com", "12345");
    createdCustomer = customersApi.createCustomer(customer);

    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(createdCustomer)
        .clickFormLink();

    // When
    addUpdateFormSteps.clickDelete();
    List<Customer> customers = customersApi.getCustomers();

    // Then
    assertAll(
        () -> assertFalse(customers.contains(createdCustomer))
    );
  }

  @Test
  void shouldDeleteRecord_AfterClick_RecordIsNotVisibleOnList() throws RestApiException {
    // Given
    Customer customer = new Customer("test", "test@test.com", "12345");
    createdCustomer = customersApi.createCustomer(customer);

    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(createdCustomer)
        .clickFormLink();

    // When
    addUpdateFormSteps.clickDelete();
    List<Customer> customers = customerTableSteps.getAllCustomers();

    // Then
    assertFalse(customers.contains(createdCustomer));
  }

  @Test
  void shouldAddRecord_AfterClick_CorrectlyAdded() throws RestApiException {
    // Given
    createdCustomer = new Customer("test", "test@test.com", "12345");

    addUpdateFormSteps.openFormPage()
        .typeData(createdCustomer);

    // When
    addUpdateFormSteps.clickSave();
    List<Customer> customers = customersApi.getCustomers();

    createdCustomer = customers.get(
        customers.indexOf(createdCustomer)
    );

    // Then
    assertAll(
        () -> assertTrue(customers.contains(createdCustomer))
    );
  }

  @Test
  void shouldAddRecord_AfterClick_CustomerVisibleOnPage() throws RestApiException {
    // Given
    createdCustomer = new Customer("test", "test@test.com", "12345");

    addUpdateFormSteps.openFormPage()
        .typeData(createdCustomer);

    // When
    addUpdateFormSteps.clickSave();
    List<Customer> customers = customerTableSteps.getAllCustomers();

    List<Customer> apiCustomers = customersApi.getCustomers();
    createdCustomer = apiCustomers.get(
        apiCustomers.indexOf(createdCustomer)
    );

    // Then
    assertTrue(customers.contains(createdCustomer));
  }

  @Test
  void shouldUpdateRecord_AfterSaveClick_CustomerUpdatedInBackend() throws RestApiException {
    // Given
    createdCustomer = customersApi.createCustomer(
        new Customer("test", "test@test.com", "test")
    );

    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(createdCustomer)
        .clickFormLink();
    addUpdateFormSteps.typeData("Changed", "Changed", "Changed");
    Customer changedCustomer = new Customer("testChanged", "test@test.comChanged", "testChanged");

    // When
    addUpdateFormSteps.clickSave();
    boolean isSelected = customerTableSteps.isSelected();
    List<Customer> customers = customersApi.getCustomers();

    // Then
    assertAll(
        () -> assertFalse(isSelected),
        () -> assertTrue(customers.contains(changedCustomer))
    );
  }

  @Test
  void shouldUpdateRecord_AfterSaveClick_CustomerUpdatedOnPagety() throws RestApiException {
    // Given
    createdCustomer = customersApi.createCustomer(
        new Customer("test", "test@test.com", "test")
    );

    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(createdCustomer)
        .clickFormLink();
    addUpdateFormSteps.typeData("Changed", "Changed", "Changed");
    Customer changedCustomer = new Customer("testChanged", "test@test.comChanged", "testChanged");

    // When
    addUpdateFormSteps.clickSave();
    boolean isSelected = customerTableSteps.isSelected();
    List<Customer> customers = customerTableSteps.getAllCustomers();

    // Then
    assertAll(
        () -> assertFalse(isSelected),
        () -> assertTrue(customers.contains(changedCustomer))
    );
  }

  @Disabled
  @ParameterizedTest
  @ArgumentsSource(CustomerDataProvider.class)
  void shouldDeselectRecordAndEmptyForm_When_CancelButtonIsClicked(Customer customer) {
    // Given
    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(customer)
        .clickFormLink();

    // When
    addUpdateFormSteps.clickCancel();
    boolean isSelected = customerTableSteps.isCustomerSelected(customer);
    boolean isAddState = addUpdateFormSteps.isFormInAddState();
    boolean areFormsEmpty = addUpdateFormSteps.areFieldsEmpty();

    // Then
    assertAll(
        () -> assertFalse(isSelected),
        () -> assertTrue(isAddState),
        () -> assertTrue(areFormsEmpty)
    );
  }

  @ParameterizedTest
  @ArgumentsSource(CustomerDataProvider.class)
  void shouldDeselectRecord_When_CancelButtonIsClicked(Customer customer) {
    // Given
    AddUpdateFormSteps addUpdateFormSteps = customerTableSteps.openHomePage()
        .clickOnCustomer(customer)
        .clickFormLink();

    // When
    addUpdateFormSteps.clickCancel();
    boolean isSelected = customerTableSteps.isCustomerSelected(customer);

    // Then
    assertFalse(isSelected);
  }

  @Override
  public void prepareSteps() {
    addUpdateFormSteps = new AddUpdateFormSteps(getDriver());
    customerTableSteps = new CustomerTableSteps(getDriver());
  }
}
