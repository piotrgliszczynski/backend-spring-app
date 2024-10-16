
export const isValidText = (text) => {
  const emptyRegex = /^\s*$/;
  return (!!text && !emptyRegex.test(text));
}

export const isValidEmail = (email) => {
  const emailRegex = /^.+@.+\..+$/i;
  return emailRegex.test(email);
}