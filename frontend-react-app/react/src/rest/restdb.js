const BASE_URL = 'http://localhost:4000';
const CUSTOMERS_ENDPOINT = 'customers';

export const getAll = async (searchQuery) => {
  try {
    const nameLike = searchQuery || "";
    const response = await fetch(`${BASE_URL}/${CUSTOMERS_ENDPOINT}?name_like=${nameLike}`);

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${BASE_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    return resopnseJson;

  } catch (error) {
    console.error('Fetching customers with error:', error);
  }
};

export const getByEmail = async (email) => {
  try {
    const emailLike = email || "";
    const response = await fetch(`${BASE_URL}/${CUSTOMERS_ENDPOINT}?email=${emailLike}`);

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${BASE_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    if (resopnseJson.length === 1) {
      return resopnseJson[0];
    }
    return {};

  } catch (error) {
    console.error('Fetching customers with error:', error);
  }
}

export const post = async (customer) => {
  try {
    const jsonBody = JSON.stringify(customer);

    const response = await fetch(`${BASE_URL}/${CUSTOMERS_ENDPOINT}`,
      {
        method: 'POST',
        headers: {
          "Content-Type": 'application/json'
        },
        body: jsonBody
      });

    if (!response.ok) {
      throw new Error(`Could not post data to ${BASE_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Creating customer with error:', error);
  }
}

export const put = async (customer) => {
  try {
    const jsonBody = JSON.stringify(customer);

    const response = await fetch(`${BASE_URL}/${CUSTOMERS_ENDPOINT}/${customer.id}`,
      {
        method: 'PUT',
        headers: {
          "Content-Type": 'application/json'
        },
        body: jsonBody
      });

    if (!response.ok) {
      throw new Error(`Could not update data to ${BASE_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Updating customer with error:', error);
  }
}

export const deleteById = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/${CUSTOMERS_ENDPOINT}/${id}`,
      {
        method: 'DELETE'
      });

    if (!response.ok) {
      throw new Error(`Could not delete data from ${BASE_URL}/${CUSTOMERS_ENDPOINT}`);
    }
  } catch (error) {
    console.error('Deleting customer with error:', error);
  }
}