'use strict';
(() => {
  const loginForm = document.querySelector('#login form');
  if (!loginForm) {
    return;
  }
  loginForm.addEventListener('submit', loginAction);

  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com';

  function loginAction(event) {
    event.preventDefault();
    loginPost();
  }

  //this function will run asynchronously
  const loginPost = async () => {
    const userName = document.getElementById('username_field').value;
    const password = document.getElementById('password_field').value;

    const loginObj = {
      userName,
      password,
    };

    // wait until fetch fetches
    const response = await fetch(`${API_URL}/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(loginObj),
    });

    const result = await response.json();

    if (!response.ok) {
      ErrorModal.show(result.message);
      console.log(result);
    } else {
      CookieManager.setCookie(result.id, result.userName);
      location.href = '/index.html';
    }
  };
})();
