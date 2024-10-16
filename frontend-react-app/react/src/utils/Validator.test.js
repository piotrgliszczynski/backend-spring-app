import { isValidText, isValidEmail } from './Validator';
import '@testing-library/jest-dom';

describe('Validator', () => {
  it('Should return valid text when text entered', () => {
    // Given
    const validText = "Test";

    // When
    const actualResult = isValidText(validText);

    // Then
    expect(actualResult).toBe(true);
  });

  describe('Should return invalid text', () => {
    it('When text is empty string', () => {
      // Given
      const emptyText = "";

      // When
      const actualResult = isValidText(emptyText);

      // Then
      expect(actualResult).toBe(false);
    });

    it('When text is blank string', () => {
      // Given
      const blankText = "   ";

      // When
      const actualResult = isValidText(blankText);

      // Then
      expect(actualResult).toBe(false);
    });

    it('When text is null/undefined string', () => {
      // Given
      const nullText = null;
      const undefinedText = undefined;

      // When
      const actualResultNull = isValidText(nullText);
      const actualResultUdefined = isValidText(undefinedText);

      // Then
      expect(actualResultNull).toBe(false);
      expect(actualResultUdefined).toBe(false);
    });
  });

  it("Should return valid email", () => {
    // Given
    const validEmail = 'test@test.com';

    // When
    const actualResult = isValidEmail(validEmail);

    // Then
    expect(actualResult).toBe(true);
  });

  describe("Invalid email", () => {
    it('Should return invalid when no "@" provided', () => {
      // Given
      const invalidEmail = 'testtest.com';

      // When
      const actualResult = isValidEmail(invalidEmail);

      // Then
      expect(actualResult).toBe(false);
    });

    it('Should return invalid when no . provided', () => {
      // Given
      const invalidEmail = 'test@testcom';

      // When
      const actualResult = isValidEmail(invalidEmail);

      // Then
      expect(actualResult).toBe(false);
    })
  })
})