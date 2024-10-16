import { enableFetchMocks } from 'jest-fetch-mock';
import { deleteById, getAll, getByEmail, post, put } from './restdb';
import '@testing-library/jest-dom';
import { waitFor } from '@testing-library/react';

enableFetchMocks();

describe("Rest requests", () => {

  beforeAll(() => {
    jest.spyOn(console, 'error').mockImplementation(() => { });
  });

  beforeEach(() => {
    fetch.resetMocks();
  });

  afterEach(() => {
    console.error.mockClear();
  });

  afterAll(() => {
    console.error.mockRestore();
  });

  describe("Fetching data", () => {

    it("should correctly fetch all data", () => {
      // Given
      const returnData = [
        {
          "id": 0,
          "name": "Mary Jackson",
          "email": "maryj@abc.com",
          "password": "maryj"
        }];
      fetch.mockResponseOnce(JSON.stringify(returnData));

      // When
      getAll()
        .then(response => {
          expect(response).toEqual(returnData);
        });

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?name_like=');
    });

    it("should correctly fetch filtered data", () => {
      // Given
      const returnData = [
        {
          "id": 0,
          "name": "Mary Jackson",
          "email": "maryj@abc.com",
          "password": "maryj"
        }];
      fetch.mockResponseOnce(JSON.stringify(returnData));

      // When
      getAll("ma")
        .then(response => {
          expect(response).toEqual(returnData);
        });

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?name_like=ma');
    });

    it("Should fail when error during fetch returned", async () => {
      // Given
      const errorMessage = "Test error message";
      fetch.mockRejectOnce(new Error(errorMessage));

      // When
      await getAll();

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?name_like=');
    });

    it("Should fail when error response code returned", async () => {
      // Given
      const errorMessage = "Could not fetch data from";
      fetch.mockResponseOnce(JSON.stringify('Internal server error'), { status: 500 });

      // When
      await getAll();

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?name_like=');
    });

    it("Should correctly fetch user by email", async () => {
      // Given
      const returnData = [{
        "id": 0,
        "name": "Mary Jackson",
        "email": "maryj@abc.com",
        "password": "maryj"
      }];
      fetch.mockResponseOnce(JSON.stringify(returnData));

      // When
      getByEmail(returnData[0].email)
        .then(response => {
          expect(response).toEqual(returnData[0]);
        });

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers?email=${returnData[0].email}`);
    });

    it("Should fail when error during fetch by email returned", async () => {
      // Given
      const errorMessage = "Test error message";
      fetch.mockRejectOnce(new Error(errorMessage));

      // When
      await getByEmail("test@test.com");

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?email=test@test.com');
    });

    it("Should fail when error response code returned", async () => {
      // Given
      const errorMessage = "Could not fetch data from";
      fetch.mockResponseOnce(JSON.stringify('Internal server error'), { status: 500 });

      // When
      await getByEmail("test@test.com");

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?email=test@test.com');
    });

    it("Should query empty email when no email provided", async () => {
      // Given
      fetch.mockResponseOnce(JSON.stringify([]));

      // When
      const response = await getByEmail();

      // Then
      expect(response).toEqual({});
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers?email=');
    });
  });

  describe("Creating data", () => {
    it("Should return success when data created correctly", () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test"
      };
      const returnData = {
        name: "test",
        email: "test@test.com",
        password: "test",
        id: 3
      };

      fetch.mockResponseOnce(JSON.stringify(returnData));

      // When
      post(requestData)
        .then(response => {
          expect(response).toEqual(returnData);
        });

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers')
      expect(fetch.mock.calls[0][1].method).toEqual('POST');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    });

    it("Should fail when error during post", async () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test"
      };
      const errorMessage = "Test error message";
      fetch.mockRejectOnce(new Error(errorMessage));

      // When
      await post(requestData);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers')
      expect(fetch.mock.calls[0][1].method).toEqual('POST');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    });

    it("Should fail when error response code returned", async () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test"
      };
      const errorMessage = "Could not post data to";
      fetch.mockResponseOnce(JSON.stringify('Internal server error'), { status: 500 });

      // When
      await post(requestData);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual('http://localhost:4000/customers')
      expect(fetch.mock.calls[0][1].method).toEqual('POST');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    })
  });

  describe("Updating data", () => {
    it("Should return success when data updated correctly", () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test",
        id: 3
      };
      const returnData = {
        name: "test",
        email: "test@test.com",
        password: "test",
        id: 3
      };

      fetch.mockResponseOnce(JSON.stringify(returnData));

      // When
      put(requestData)
        .then(response => {
          expect(response).toEqual(returnData);
        });

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${requestData.id}`);
      expect(fetch.mock.calls[0][1].method).toEqual('PUT');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    });

    it("Should fail when error during post", async () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test",
        id: 3
      };
      const errorMessage = "Test error message";
      fetch.mockRejectOnce(new Error(errorMessage));

      // When
      await put(requestData);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${requestData.id}`)
      expect(fetch.mock.calls[0][1].method).toEqual('PUT');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    });

    it("Should fail when error response code returned", async () => {
      // Given
      const requestData = {
        name: "test",
        email: "test@test.com",
        password: "test",
        id: 3
      };
      const errorMessage = "Could not update data to";
      fetch.mockResponseOnce(JSON.stringify('Internal server error'), { status: 500 });

      // When
      await put(requestData);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${requestData.id}`)
      expect(fetch.mock.calls[0][1].method).toEqual('PUT');
      expect(fetch.mock.calls[0][1].body).toEqual(JSON.stringify(requestData));
    })
  });

  describe("Deleting data", () => {
    it("Should return success when data deleted correctly", () => {
      // Given
      const customerId = 3;
      fetch.mockResponseOnce(JSON.stringify({}));

      // When
      deleteById(customerId);

      // Then
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${customerId}`);
      expect(fetch.mock.calls[0][1].method).toEqual('DELETE');
    });

    it("Should fail when error during post", async () => {
      // Given
      const customerId = 3;
      const errorMessage = "Test error message";
      fetch.mockRejectOnce(new Error(errorMessage));

      // When
      await deleteById(customerId);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${customerId}`);
      expect(fetch.mock.calls[0][1].method).toEqual('DELETE');
    });

    it("Should fail when error response code returned", async () => {
      // Given
      const customerId = 3;
      const errorMessage = "Could not delete data from";
      fetch.mockResponseOnce(JSON.stringify('Internal server error'), { status: 500 });

      // When
      await deleteById(customerId);

      // Then
      expect(console.error).toHaveBeenCalledTimes(1);
      expect(console.error.mock.calls[0][1].toString()).toContain(errorMessage);
      expect(fetch.mock.calls.length).toEqual(1);
      expect(fetch.mock.calls[0][0]).toEqual(`http://localhost:4000/customers/${customerId}`);
      expect(fetch.mock.calls[0][1].method).toEqual('DELETE');
    })
  });
});