const DATA_API_URL = 'http://localhost:8080';
const EVENTS_ENDPOINT = 'api/registrations';

export const registerForEvent = async (event, token) => {
  try {
    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}/event/${event.id}`,
      {
        method: 'POST',
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });

    if (!response.ok) {
      throw new Error(`Could not post data to ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Creating event with error:', error);
  }
};

export const getAllByEvent = async (event, token) => {
  try {
    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}/event/${event.id}`,
      {
        headers: { 'Authorization': `Bearer ${token}` }
      });

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    return resopnseJson;

  } catch (error) {
    console.error('Fetching registrations with error:', error);
  }
};

export const getAllByCustomer = async (token) => {
  try {
    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}/customer`,
      {
        headers: { 'Authorization': `Bearer ${token}` }
      });

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    return resopnseJson;

  } catch (error) {
    console.error('Fetching registrations with error:', error);
  }
};