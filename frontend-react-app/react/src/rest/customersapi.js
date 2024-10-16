const DATA_API_URL = 'http://localhost:8080';
const CUSTOMERS_ENDPOINT = 'api/customers';

export const getAll = async (searchQuery, token) => {
  try {
    const nameLike = searchQuery || "";
    const response = await fetch(`${DATA_API_URL}/${CUSTOMERS_ENDPOINT}?name_like=${nameLike}`,
      {
        headers: { 'Authorization': `Bearer ${token}` }
      });

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    return resopnseJson;

  } catch (error) {
    console.error('Fetching customers with error:', error);
  }
};

export const post = async (customer, token) => {
  try {
    const jsonBody = JSON.stringify(customer);

    const response = await fetch(`${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`,
      {
        method: 'POST',
        headers: {
          "Content-Type": 'application/json',
          "Authorization": `Bearer ${token}`
        },
        body: jsonBody
      });

    if (!response.ok) {
      throw new Error(`Could not post data to ${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Creating customer with error:', error);
  }
}

export const put = async (customer, token) => {
  try {
    const jsonBody = JSON.stringify(customer);

    const response = await fetch(`${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`,
      {
        method: 'PUT',
        headers: {
          "Content-Type": 'application/json',
          "Authorization": `Bearer ${token}`
        },
        body: jsonBody
      });

    if (!response.ok) {
      throw new Error(`Could not update data to ${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Updating customer with error:', error);
  }
}

export const deleteById = async (id, token) => {
  try {
    const response = await fetch(`${DATA_API_URL}/${CUSTOMERS_ENDPOINT}/${id}`,
      {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

    if (!response.ok) {
      throw new Error(`Could not delete data from ${DATA_API_URL}/${CUSTOMERS_ENDPOINT}`);
    }
  } catch (error) {
    console.error('Deleting customer with error:', error);
  }
}