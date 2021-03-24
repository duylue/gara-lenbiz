$(document).ready(function () {
  initHeaderMenu();
});
function initHeaderMenu() {
  var target = '.js-header-item'
  $(document).on('click', '.js-header-item__btn', function () {
    $(this).parents(target).toggleClass('is-open');
  });
  // popUp閉じる
  $(document).on('click', function (e) {
    if (!$(e.target).closest(target).length) {
      $(target).removeClass('is-open');
    }
  });
};

function isValidatePassword(pass) {
  var re = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@!%*#^&])[A-Za-z\d@!%*#^&]{8,32}$/;
      return re.test(pass);
}

function isValidateEmail(email) {
  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  return re.test(email);
}

function validatePhone(phone) {
	  var re = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4,5})$/;
	  return re.test(phone);
}

function isEmpty(text) {
  if (text == "" || text == null) {
    return true;
  } else {
    return false;
  }
}

function getUrlParameter(sParam) {
  var sPageURL = window.location.search.substring(1),
      sURLVariables = sPageURL.split('&'),
      sParameterName,
      i;

  for (i = 0; i < sURLVariables.length; i++) {
      sParameterName = sURLVariables[i].split('=');

      if (sParameterName[0] === sParam) {
          return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
      }
  }
};