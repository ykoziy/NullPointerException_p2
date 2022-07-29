(() => {
  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com';

  const getUserAddress = async () => {
    const response = await fetch(
      `${API_URL}/address/uid/${CookieManager.getUserId()}`,
    );
    const result = await response.json();

    if (!response.ok) {
      console.log(result);
      return;
    } else {
      createAddressItem(result[0]);
    }
  };

  function updateTotal() {
    document.getElementById('sub-total').innerText = Cart.getTotal();
  }

  updateTotal();

  const addCreditCard = async (cardInput) => {
    const cookie = CookieManager.getUserinfo();
    let cardObj = {
      type: 'Visa',
      holderFirstName: cookie.firstName,
      holderLastName: cookie.lastName,
    };

    const newCardObj = { ...cardObj, ...cardInput };

    const response = await fetch(
      `${API_URL}/cards/add/${CookieManager.getUserId()}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newCardObj),
      },
    );

    const result = await response.json();

    if (!response.ok) {
      ErrorModal.show(result.message);
      console.log(result);
      return null;
    } else {
      return result;
    }
  };

  const placeOrder = async (cardObj) => {
    const cardResult = await addCreditCard(cardObj);

    if (cardResult) {
      const orderObj = {};
      orderObj.amount = Cart.getTotal();
      orderObj.products = [];
      const cartItems = Cart.getCart();
      for (const item of cartItems) {
        for (let i = 0; i < item.quantity; i++) {
          orderObj.products.push({ id: item.id });
        }
      }

      const response = await fetch(
        `${API_URL}/orders/add/${CookieManager.getUserId()}/${cardResult.id}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(orderObj),
        },
      );

      const result = await response.json();

      if (!response.ok) {
        ErrorModal.show(result.message);
        console.log(result);
        return;
      } else {
        return result;
      }
    }
  };

  const validateCardEntry = () => {
    const inputs = document.querySelectorAll('#form_left input');
    let numFieldsWrong = 0;
    for (let input of inputs) {
      if (input.checkValidity()) {
        input.style.backgroundColor = 'white';
      } else {
        input.style.backgroundColor = 'rgba(255, 60, 58, 0.8)';
        numFieldsWrong++;
      }
    }
    return numFieldsWrong;
  };

  const orderAction = async () => {
    if (validateCardEntry() === 0) {
      const ccNum = document.querySelector('#num_field').value;
      const expirationDate = document
        .querySelector('#expiration_field')
        .value.split('/');

      cardObj = {
        expMonth: expirationDate[0],
        expYear: expirationDate[1],
        number: ccNum.split(' ').join(''),
      };
      const orderResult = await placeOrder(cardObj);
      Cart.clear();
      location.href = '/order_confirmation.html';
    }
  };

  const createAddressItem = (address) => {
    const cartItem = document.createElement('div');
    cartItem.dataset.id = address.id;
    const addressTemplate = `
         <div>
         <b>Ship to:</b>
         </div>
         <div>${
           CookieManager.getUserinfo().firstName +
           ' ' +
           CookieManager.getUserinfo().lastName
         }</div>
         <div>${address.street}</div>
         <div>${address.city}, ${address.state} ${address.zip}</div>
       `;
    document.querySelector('#address-info').innerHTML = addressTemplate;
  };

  getUserAddress();

  document
    .querySelector('#place-order-btn')
    .addEventListener('click', orderAction);
})();
