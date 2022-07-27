'use strict';
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
    cookieArray.forEach((val) => {
      if (val.indexOf(name) === 0) {
        res = val.substring(name.length);
      }
    });
    return JSON.parse(res);
  }

  static getUserId() {
    return this.getCookieUserinfo().id;
  }

  static getUsername() {
    return this.getCookieUserinfo().userName;
  }

  static delete() {
    document.cookie =
      'userinfo=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
  }
}

class Cart {
  constructor() {
    this.cart = [];
  }

  add(item) {
    const foundItem = this.getItem(item.id);
    if (foundItem) {
      foundItem.quantity++;
    } else {
      const quant = { quantity: 1 };
      const cartItem = { ...item, ...quant };
      this.cart.push(cartItem);
    }
  }

  remove(itemId) {
    for (let i = this.cart.length - 1; i >= 0; i--) {
      if (this.cart[i].id === itemId) {
        this.cart.splice(i, 1);
        break;
      }
    }
  }

  clear() {
    this.cart = [];
  }

  changeQuantity(itemId, isAdding = true) {
    const item = this.cart.find((item) => item.id === itemId);
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
  }

  getItem(itemId) {
    return this.cart.find((item) => item.id === itemId);
  }

  getCart() {
    return this.cart;
  }

  getItemTotal(itemId) {
    const item = this.cart.find((item) => item.id === itemId);
    return item.quantity * item.price;
  }

  getTotal() {
    return this.cart.reduce((prev, curr) => {
      return prev + curr.price * curr.quantity;
    }, 0);
  }
}

/* 
const itemA = {
  id: 'product_id_A',
  name: 'Super CPU AZ1',
  price: 956.99,
};

const cart = new Cart();

cart.add(itemA);
cart.add(itemA);
console.log(cart.getCart());
console.log(cart.getTotal());
 */
