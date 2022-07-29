'use strict';
const STATES = [
  'AL',
  'AK',
  'AZ',
  'AR',
  'CA',
  'CO',
  'CT',
  'DE',
  'FL',
  'GA',
  'HI',
  'ID',
  'IL',
  'IN',
  'IA',
  'KS',
  'KY',
  'LA',
  'ME',
  'MD',
  'MA',
  'MI',
  'MN',
  'MS',
  'MO',
  'MT',
  'NE',
  'NV',
  'NH',
  'NJ',
  'NM',
  'NY',
  'NC',
  'ND',
  'OH',
  'OK',
  'OR',
  'PA',
  'RI',
  'SC',
  'SD',
  'TN',
  'TX',
  'UT',
  'VT',
  'VA',
  'WA',
  'WV',
  'WI',
  'WY',
];

(() => {
  const registerForm = document.querySelector('#register form');
  if (!registerForm) {
    return;
  }
  registerForm.addEventListener('submit', loginAction);

  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com';

  function loginAction(event) {
    event.preventDefault();
    validateForm(insertAddress);
  }

  function validateForm(callback) {
    const formObj = {};
    const inputs = document.querySelectorAll('form input:not([type="submit"])');
    for (const input of inputs) {
      formObj[`${input.id.split('_', 1)}`] = input.value.trim();
    }

    formObj.state = formObj.state.toUpperCase();
    if (!STATES.includes(formObj.state)) {
      ErrorModal.show(`State ${formObj.state} does not exist. Try again.`);
      console.log('invalid state');
      return;
    }

    if (formObj.repass !== formObj.pass) {
      ErrorModal.show('Passwords dont match');
      console.log('Passwords dont match. Try again.');
      return;
    }

    const splitName = formObj.name.split(' ');

    const registerUserObj = {
      firstName: splitName[0],
      lastName: splitName[1],
      userName: formObj.username,
      phone: formObj.tel,
      email: formObj.email,
      password: formObj.pass,
    };

    const addressObj = {
      street: formObj.street,
      city: formObj.city,
      state: formObj.state,
      zip: formObj.zip,
    };

    callback(registerUserObj, addressObj);
  }

  const registerUser = async (userInformation) => {
    const response = await fetch(`${API_URL}/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userInformation),
    });

    const result = await response.json();

    if (!response.ok) {
      ErrorModal.show(result.message);
      console.log(result);
      return null;
    } else {
      return result;
    }
  };

  const insertAddress = async (userInformation, addressInformation) => {
    const res = await registerUser(userInformation);
    if (res) {
      const requestUrl = `${API_URL}/address/add/${res.id}`;
      const response = await fetch(requestUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(addressInformation),
      });

      if (!response.ok) {
        ErrorModal.show(result.message);
        console.log(result);
        return;
      } else {
        CookieManager.setCookie(res);
        location.href = '/index.html';
      }
    }
  };
})();
