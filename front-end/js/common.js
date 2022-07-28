'use strict';
class CookieManager {
  static setCookie(id, userName) {
    const d = new Date();
    d.setTime(d.getTime() + 12 * 60 * 60 * 1000);
    const expires = d.toUTCString();
    const cookieObj = {
      id,
      userName,
    };

    document.cookie = `userinfo=${JSON.stringify(
      cookieObj,
    )}; expires=${expires}; path=/`;
  }

  static getUserinfo() {
    const name = 'userinfo=';
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split('; ');
    let res;
    if (document.cookie.length === 0) {
      return null;
    }
    cookieArray.forEach((val) => {
      if (val.indexOf(name) === 0) {
        res = val.substring(name.length);
      }
    });
    return JSON.parse(res);
  }

  static getUserId() {
    if (this.getUserinfo()) {
      return this.getUserinfo().id;
    }
    return null;
  }

  static getUsername() {
    if (this.getUserinfo()) {
      return this.getUserinfo().id;
    }
    return null;
  }

  static delete() {
    document.cookie =
      'userinfo=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
  }
}

(() => {
  const cartIcon = document.querySelector('li .cart-icon').parentElement;
  const loginRegistrationLink = document.getElementById('logreg');
  if (!CookieManager.getUserId()) {
    cartIcon.hidden = true;
    loginRegistrationLink.innerHTML = '<a href="/login.html">Login</a>';
  } else {
    cartIcon.hidden = false;
    loginRegistrationLink.innerHTML =
      '<a href="/myaccount.html">My Account</a>';
  }
})();

class ErrorModal {
  static show(errorMessage) {
    const modalContainer = document.createElement('div');
    modalContainer.classList.add('modal-container');

    const modal = document.createElement('div');
    modal.classList.add('error-modal');

    const msg = document.createElement('h1');
    msg.innerText = errorMessage;

    modal.appendChild(msg);
    modalContainer.prepend(modal);

    const mainElement = document.querySelector('main');

    modal.addEventListener('click', this.hide);
    document.body.insertBefore(modalContainer, mainElement);
  }

  static hide() {
    const modalContainer = document.querySelector('.modal-container');
    if (modalContainer) {
      modalContainer.remove();
    }
  }
}

class Cart {
  constructor() {
    sessionStorage.removeItem('cart');
  }

  add(item) {
    const cart = [...this.#unpackCart()];
    let foundItem = cart.find((i) => i.id === item.id);
    if (foundItem) {
      foundItem.quantity++;
    } else {
      const quant = { quantity: 1 };
      const cartItem = { ...item, ...quant };
      cart.push(cartItem);
    }
    this.#packCart(cart);
  }

  remove(itemId) {
    let cart = [...this.#unpackCart()];
    console.log(cart);
    for (let i = cart.length - 1; i >= 0; i--) {
      if (cart[i].id === itemId) {
        cart.splice(i, 1);
        break;
      }
    }
    this.#packCart(cart);
  }

  clear() {
    this.cart = [];
    sessionStorage.removeItem('cart');
  }

  changeQuantity(itemId, isAdding = true) {
    let cart = this.#unpackCart();
    const item = cart.find((item) => item.id === itemId);
    if (!item) {
      return;
    }

    if (isAdding) {
      item.quantity++;
    } else {
      item.quantity--;
      if (item.quantity === 0) {
        this.remove(item.id);
      }
    }
    this.#packCart(cart);
  }

  getItem(itemId) {
    let cart = [...this.#unpackCart()];
    return cart.find((item) => item.id === itemId);
  }

  getCart() {
    let cart = [...this.#unpackCart()];
    return cart;
  }

  getItemTotal(itemId) {
    let cart = [...this.#unpackCart()];
    const item = cart.find((item) => item.id === itemId);
    return item.quantity * parseFloat(item.price);
  }

  getTotal() {
    let cart = [...this.#unpackCart()];
    return cart.reduce((prev, curr) => {
      parseInt;
      return prev + parseFloat(curr.price) * parseFloat(curr.quantity);
    }, 0);
  }

  #packCart(cart) {
    const jsonCart = JSON.stringify(cart);
    sessionStorage.setItem('cart', jsonCart);
  }

  #unpackCart() {
    let cartSession = sessionStorage.getItem('cart');
    let cartItems = JSON.parse(cartSession);
    if (cartItems != null) {
      return JSON.parse(cartSession);
    } else {
      return [];
    }
  }
}
