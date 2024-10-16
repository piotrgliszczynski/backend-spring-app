const DATA_API_URL = 'http://localhost:8080';
const EVENTS_ENDPOINT = 'api/events';

export const getAll = async (token) => {
  try {
    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}`,
      {
        headers: { 'Authorization': `Bearer ${token}` }
      });

    if (!response.ok) {
      throw new Error(`Could not fetch data from ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }

    const resopnseJson = await response.json();
    return resopnseJson;

  } catch (error) {
    console.error('Fetching events with error:', error);
  }
};

export const post = async (event, token) => {
  try {
    const jsonBody = JSON.stringify(event);

    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}`,
      {
        method: 'POST',
        headers: {
          "Content-Type": 'application/json',
          "Authorization": `Bearer ${token}`
        },
        body: jsonBody
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

export const put = async (event, token) => {
  try {
    const jsonBody = JSON.stringify(event);

    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}`,
      {
        method: 'PUT',
        headers: {
          "Content-Type": 'application/json',
          "Authorization": `Bearer ${token}`
        },
        body: jsonBody
      });

    if (!response.ok) {
      throw new Error(`Could not put data to ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }

    const responseJson = await response.json();
    return responseJson;
  } catch (error) {
    console.error('Updating event with error:', error);
  }
};

export const deleteById = async (id, token) => {
  try {

    const response = await fetch(`${DATA_API_URL}/${EVENTS_ENDPOINT}/${id}`,
      {
        method: 'DELETE',
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });

    if (!response.ok) {
      throw new Error(`Could not delete data from ${DATA_API_URL}/${EVENTS_ENDPOINT}`);
    }
  } catch (error) {
    console.error('Deleting event with error:', error);
  }
}