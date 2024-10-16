const AUTHENTICATION_API_URL = 'http://localhost:8081';
const LOGIN_ENDPOINT = "account/token";
const REGISTER_ENDPOINT = "account/register";

export const getToken = async (customer) => {
  try {
    const response = await fetch(`${AUTHENTICATION_API_URL}/${LOGIN_ENDPOINT}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer)
      });

    if (!response.ok) {
      throw new Error(`Could not log in using provided credentials`);
    }

    const responseJson = await response.json();
    return responseJson.access_token;
  } catch (error) {
    console.error(error);
  }
}

export const register = async (customer) => {
  try {
    const response = await fetch(`${AUTHENTICATION_API_URL}/${REGISTER_ENDPOINT}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer)
      });

    if (!response.ok) {
      throw new Error(`Could not regsiter new user!`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error(error);
  }
}