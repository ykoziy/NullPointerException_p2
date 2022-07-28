//! image placeholder
//! https://placeholder.pics/svg/141x160/E4FF92-79FFE0/FF8746-000000/no%20img%20%3A(
//! gaming, systems, components, electronics
//! _selected

(() => {
  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com/product/category';

  class ProducstNav {
    constructor() {
      // select category buttons
      this.categoryBtns = document.querySelectorAll(
        '#gaming, #systems, #components, #electronics',
      );
      this.activeBtnIndex = 0;
      this.navNames = ['gaming', 'systems', 'components', 'electronics'];
      this.#setEventListeners();
    }

    #setEventListeners() {
      for (let btn of this.categoryBtns) {
        btn.addEventListener('click', (evt) => {
          this.#btnClickAction(evt.currentTarget);
        });
      }
    }

    #clearActive() {
      for (let btn of this.categoryBtns) {
        btn.classList.remove('selected-card-btn');
        const newIconName = `img/${btn.id}.svg`;
        btn.querySelector('img').setAttribute('src', newIconName);
      }
    }

    #btnClickAction(target) {
      this.#clearActive();
      const newIconName = `img/${target.id}_selected.svg`;
      target.querySelector('img').setAttribute('src', newIconName);
      getProducts(target.id);
    }
  }

  new ProducstNav();

  const createProductCard = (product) => {
    const productCard = document.createElement('div');

    productCard.classList.add('product-card');
    productCard.dataset.id = product.id;

    const productContainer = document.createElement('div');
    productContainer.classList.add('product-card-container');
    productCard.appendChild(productContainer);

    const cardTemplate = `
         <div class="product-image">
            <img src="https://placeholder.pics/svg/141x160/E4FF92-79FFE0/FF8746-000000/no%20img%20%3A(" />
         </div>
         <div class="product-description">
            <p class="body">
            <b>${product.name}:</b>${product.description}</p>
         </div>
    `;

    const productMod = `
    <div class="product-mod-container">
       <div class="product-q-minus" data-id=${product.id}></div>
       <div class="product-q">1</div>
       <div class="product-q-plus" data-id=${product.id}></div>
    </div>
      `;

    const priceH = `<h2 class="styled">$${product.price}</h2>`;
    const button = `<button class="add-cart-btn" data-id=${product.id}>Add to Cart</button>`;

    productContainer.innerHTML = cardTemplate;
    productCard.insertAdjacentHTML('beforeend', priceH);
    productCard.insertAdjacentHTML('beforeend', productMod);
    productCard.insertAdjacentHTML('beforeend', button);
    return productCard;
  };

  const createProductCards = (products) => {
    const con = document.querySelector('#items-container');
    con.innerHTML = '';
    //const line = '<img class="line" src="img/lines.png" />';
    products.forEach((i) => {
      con.appendChild(createProductCard(i));
    });
  };

  const getProducts = async (categoryName) => {
    const response = await fetch(`${API_URL}/${categoryName}`);

    const result = await response.json();

    if (!response.ok) {
      console.log(result);
      return;
    } else {
      createProductCards(result);
    }
  };

  getProducts('components');
})();
