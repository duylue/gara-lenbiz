$(document).ready(function () {

  if (!isEmpty($("#email").val().trim())) {
    $("#title-email").css("top","0px");
  };
  if (!isEmpty($("#password").val().trim())) {
    $("#title-password").css("top", "0px");
  };

    $("#btn-login").click(function () {
        login();
    });

    document.addEventListener("keyup", function(event) {
        event.preventDefault();
        if (event.keyCode === 13) {
        	login();
        }
    });

    $("#email").focus(function() {
      if (isEmpty($("#email").val().trim())) {
        animationTitle($("#title-email"), "35px");
      };
      $("#messges-error").hide();
      $("#title-email").css("color", "#00AFEC");
    });
    $("#email").blur(function(){
      if (isEmpty($("#email").val().trim())) {
        animationTitle($("#title-email"), "-35px");
        };
        $("#title-email").css("color", "#777777");
    });

    $("#password").focus(function() {
      if (isEmpty($("#password").val().trim())) {
        animationTitle($("#title-password"), "35px");
      };
      $("#messges-error").hide();
      $("#title-password").css("color", "#00AFEC");
    });
    $("#password").blur(function() {
      if (isEmpty($("#password").val().trim())) {
        animationTitle($("#title-password"), "-35px");
      };
      $("#title-password").css("color", "#777777");
    });
});

// call api login
function login() {
  var email = $("#email").val().trim();
  var password = $("#password").val().trim();

  if (isCheckEmail(email) && isCheckPassword(password)) {
    window.location.href = "/home";
  }
}

// check validate email
function isCheckEmail(email) {
  if (!isValidateEmail(email)) {
    $("#messges-error").text(Error.mail);
    $("#messges-error").show();
    return false;
  };
    return true;
}

// check validate password
function isCheckPassword(password) {
  if (!isValidatePassword(password)) {
    $("#messges-error").text(Error.password);
    $("#messges-error").show();
    return false;
  };
    return true;
}

//animation title movie
function animationTitle(name, value) {
  name.fadeIn('slow',function(){
    $(this).animate({'top': "-=" + value},'slow');
  });
}
