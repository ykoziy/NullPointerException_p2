/* <div class="">

</div> 
*/
(() => {
  function updateTotal() {
    document.getElementById('sub-total').innerText = Cart.getTotal();
  }

  function cartAction(event, item) {
    Cart.remove(item.id);
    Cart.updateBadge();
    const cartItem = event.currentTarget.parentNode.parentNode;
    cartItem.remove();
    updateTotal();
  }

  const createCartItem = (item) => {
    const cartItem = document.createElement('div');

    cartItem.classList.add('cart-item');
    cartItem.dataset.id = item.id;

    const itemTemplate = `
      <div class="cart-img">
         <img src="${item.imageURL}" />
      </div>
      <div class="cart-item-aside">
         <div class="cart-item-name">${item.name}</div>
         <div class="cart-item-desc">${item.description}</div>
         <div clas="cart-item-quant">Quantity: <span>${item.quantity}</span></div>
         <div class="cart-item-price">${item.price}</div>
         <div class="remove-item-btn" data-id=${item.id}>Remove item</div>
      </div>
      `;

    cartItem.innerHTML = itemTemplate;
    cartItem
      .querySelector('.remove-item-btn')
      .addEventListener('click', (evt) => cartAction(evt, item));
    return cartItem;
  };

  const createCartItems = (items) => {
    const con = document.querySelector('.cart-item-container');
    con.innerHTML = '';
    //const line = '<img class="line" src="img/lines.png" />';
    items.forEach((i) => {
      con.appendChild(createCartItem(i));
    });
    updateTotal();
  };

  createCartItems(Cart.getCart());
})();
