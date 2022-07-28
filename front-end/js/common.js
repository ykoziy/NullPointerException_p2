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

  static add(item) {
    const cart = [...this.#unpackCart()];
    let foundItem = cart.find((i) => i.id === item.id);
    if (foundItem) {
      foundItem.quantity = +foundItem.quantity + parseInt(item.quantity);
    } else {
      const cartItem = { ...item };
      cart.push(cartItem);
    }
    this.#packCart(cart);
  }

  static remove(itemId) {
    let cart = [...this.#unpackCart()];
    for (let i = cart.length - 1; i >= 0; i--) {
      if (cart[i].id === itemId) {
        cart.splice(i, 1);
        break;
      }
    }
    this.#packCart(cart);
  }

  static clear() {
    this.cart = [];
    sessionStorage.removeItem('cart');
  }

  static changeQuantity(itemId, isAdding = true) {
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

  static getItem(itemId) {
    let cart = [...this.#unpackCart()];
    return cart.find((item) => item.id === itemId);
  }

  static getCart() {
    let cart = [...this.#unpackCart()];
    return cart;
  }

  static getItemTotal(itemId) {
    let cart = [...this.#unpackCart()];
    const item = cart.find((item) => item.id === itemId);
    return item.quantity * parseFloat(item.price);
  }

  static getTotal() {
    let cart = [...this.#unpackCart()];
    return cart.reduce((prev, curr) => {
      parseInt;
      return prev + parseFloat(curr.price) * parseFloat(curr.quantity);
    }, 0);
  }

  static getTotalCount() {
    let cart = [...this.#unpackCart()];
    let total = 0;
    for (const item of cart) {
      total += parseInt(item.quantity);
    }
    return total;
  }

  static #packCart(cart) {
    const jsonCart = JSON.stringify(cart);
    sessionStorage.setItem('cart', jsonCart);
  }

  static #unpackCart() {
    let cartSession = sessionStorage.getItem('cart');
    let cartItems = JSON.parse(cartSession);
    if (cartItems != null) {
      return JSON.parse(cartSession);
    } else {
      return [];
    }
  }
  static updateBadge() {
    const cartImg = document.querySelector('#cart-img');
    cartImg.setAttribute('count', this.getTotalCount());
  }
}

(() => {
  const loginRegistrationLink = document.getElementById('logreg');
  Cart.updateBadge();
  if (!CookieManager.getUserId()) {
    //cartIcon.hidden = true; ---debug mode
    loginRegistrationLink.innerHTML = '<a href="/login.html">Login</a>';
  } else {
    //cartIcon.hidden = false; ---debug mode
    loginRegistrationLink.innerHTML =
      '<a href="/myaccount.html">My Account</a>';
  }
})();
