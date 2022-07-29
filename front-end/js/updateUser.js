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
  function removeEmpty(obj) {
    const newObj = {};
    for (const key in obj) {
      if (obj[key]) {
        newObj[key] = obj[key];
      }
    }
    return newObj;
  }

  const registerForm = document.querySelector('#register form');
  if (!registerForm) {
    return;
  }
  registerForm.addEventListener('submit', updateAction);

  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com';

  function updateAction(event) {
    event.preventDefault();
    validateForm();
  }

  function validateForm() {
    const formObj = {};
    const inputs = document.querySelectorAll('form input:not([type="submit"])');
    for (const input of inputs) {
      if (input.getAttribute('disabled') !== false) {
        formObj[`${input.id.split('_', 1)}`] = input.value.trim();
      }
    }

    formObj.state = formObj.state.toUpperCase();
    if (formObj.state) {
      if (!STATES.includes(formObj.state)) {
        ErrorModal.show(`State ${formObj.state} does not exist. Try again.`);
        console.log('invalid state');
        return;
      }
    }

    if (formObj.repass.length !== 0 || formObj.pass.length !== 0) {
      if (formObj.repass !== formObj.pass) {
        ErrorModal.show('Passwords dont match');
        console.log('Passwords dont match. Try again.');
        return;
      }
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

    const filterUserInfo = removeEmpty(registerUserObj);
    const filterAddressInfo = removeEmpty(addressObj);
    if (Object.keys(filterUserInfo).length !== 0) {
      updateUser(filterUserInfo);
    }
    if (Object.keys(filterAddressInfo).length !== 0) {
      updateAddress(filterAddressInfo);
    }
  }

  const updateUser = async (userInformation) => {
    const userInfo = { ...userInformation };
    userInfo.id = CookieManager.getUserId();
    const response = await fetch(`${API_URL}/users/update`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userInfo),
    });

    const result = await response.json();

    if (!response.ok) {
      ErrorModal.show(result.message);
      console.log(result);
      return null;
    } else {
      CookieManager.setCookie(result);
    }
  };

  const getAddress = async () => {
    const response = await fetch(
      `${API_URL}/address/uid/${CookieManager.getUserId()}`,
    );

    const result = await response.json();

    if (!response.ok) {
      console.log(result);
      return;
    } else {
      return result[0];
    }
  };

  const updateAddress = async (addressInformation) => {
    const addrInfo = { ...addressInformation };
    const res = await getAddress();
    if (res) {
      addrInfo.id = res.id;
      const requestUrl = `${API_URL}/address/update`;
      const response = await fetch(requestUrl, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(addrInfo),
      });

      const result = await response.json();

      if (!response.ok) {
        ErrorModal.show(result.message);
        console.log(result);
        return;
      }
    }
  };
})();
