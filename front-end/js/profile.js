(() => {
  function formatPhone(phone) {
    return `${phone.substr(0, 3)}.${phone.substr(3, 3)}.${phone.substr(6, 4)}`;
  }

  function populateUserData() {
    const container = document.querySelector('#user_prof');
    const userInfo = CookieManager.getUserinfo();
    const profileTemplate = `
         <div><b>Email:</b> ${userInfo.email}</div>
         <div><b>Name:</b> ${userInfo.firstName} ${userInfo.lastName}</div>
         <div><b>Address:</b> 2345 Address Rd, City State 79936</div>
         <div><b>Phone:</b> ${formatPhone(userInfo.phone)}</div>
         <div>
            <a href="update_user.html"
            ><button class="buttontext">Update Info</button></a
            >
         </div>
    `;
    container.innerHTML = profileTemplate;
  }

  populateUserData();
})();
