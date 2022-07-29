(() => {
  const API_URL =
    'http://nullpointerexception-env.eba-jvp359am.us-east-1.elasticbeanstalk.com';
  function formatPhone(phone) {
    return `${phone.substr(0, 3)}.${phone.substr(3, 3)}.${phone.substr(6, 4)}`;
  }

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

  async function populateUserData() {
    const container = document.querySelector('#user_prof');
    const userInfo = CookieManager.getUserinfo();
    const addressInfo = await getAddress();
    const profileTemplate = `
         <div><h4>Member since ${CookieManager.getUserinfo().registrationDate.substr(
           0,
           4,
         )}</h4></div>
         <div><b>Email:</b> ${userInfo.email}</div>
         <div><b>Name:</b> ${userInfo.firstName} ${userInfo.lastName}</div>
         <div><b>Address:</b> ${addressInfo.street} ${addressInfo.city} ${
      addressInfo.state
    } ${addressInfo.zip}</div>
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
