package org.training.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(builder = Customer.CustomerBuilder.class)
public class Customer {

  private final int id;
  private final String name;
  private final String email;
  private final String password;

  public Customer(String name, String email, String password) {
    this.id = 0;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Customer customer = (Customer) o;

    if (!Objects.equals(name, customer.name)) return false;
    if (!Objects.equals(email, customer.email)) return false;
    return Objects.equals(password, customer.password);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class CustomerBuilder {
    private int id;
    private String name;
    private String email;
    private String password;

    public CustomerBuilder id(int id) {
      this.id = id;
      return this;
    }

    public CustomerBuilder name(String name) {
      this.name = name;
      return this;
    }

    public CustomerBuilder email(String email) {
      this.email = email;
      return this;
    }

    public CustomerBuilder password(String password) {
      this.password = password;
      return this;
    }

    public Customer build() {
      return new Customer(id, name, email, password);
    }
  }
}
