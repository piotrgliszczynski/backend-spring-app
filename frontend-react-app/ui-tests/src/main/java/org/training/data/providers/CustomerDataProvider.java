package org.training.data.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.training.data.Customer;
import org.training.utils.json.JsonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CustomerDataProvider implements ArgumentsProvider {

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
    List<Arguments> arguments = Arrays.stream(JsonUtils.deserializeJson("customers.json", Customer[].class))
        .map(Arguments::of)
        .toList();

    return arguments.stream();
  }
}
