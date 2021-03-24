var dataOtp = new Object();
$(document).ready(function () {
    
    $("#btn-get-otp").click(function() {
        callApiGetOtp();
        $("#btn-get-otp").attr('style',  'display:none');
        loading($("#btn-resend-otp"));
    })

    $("#btn-verify").click(function() {
        callApiVerifyOtp();
    })

    $("#btn-resend-otp").click(function() {
        callResendOtp();
        $("#btn-resend-otp").attr('style',  'display:none');
        loading($("#btn-resend-otp"));
    })
});

function loading(btn) {
    $(".item").attr('style', 'display:inline');
    var time = 65; /* how long the timer runs for */
        var initialOffset = '440';
        var i = 1
        var interval = setInterval(function() {
            $('.circle_animation').css('stroke-dashoffset', initialOffset-(i*(4)));
            $('h6').text(i);
            if (i == time) {
                clearInterval(interval);
            }
            i++;  
        }, 1000);
        setTimeout(function(){
            btn.attr('style', 'display:inline');
            $(".item").attr('style', 'display:none'); 
        }, 30000);
}

function callApiGetOtp() {
    $("#success_tic").modal("hide");
    // $(".loader_div").show();

    var cif = getUrlParameter("cif");
    var phone = $("#ip-mobile").val().trim();
    var urlGetOtp = "http://localhost:8092/gara/v1.0/sv-get-otp";
    var request = new Object();
    request.custId = cif;
    request.mobile = phone;

        $.ajax({
        method: "POST",
        url: urlGetOtp,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request),
        }).done(function(response){
        // $(".loader_div").hide();
            console.log(response);
            dataOtp = response.data;
            console.log(dataOtp);
        }).fail(function(result) {
            // $(".loader_div").hide();
            console.log(result)
        });
}

function callApiVerifyOtp() {
    $("#success_tic").modal("hide");
    $(".loader_div").show();

    var cif = getUrlParameter("cif");
    var otp = $("#lb-sign").val().trim();
    var urlGetOtp = "http://localhost:8092/gara/v1.0/sv-verify-otp";

    var request = new Object();
    request.otp = otp;
	request.id = dataOtp.uuId;
	request.billCode = dataOtp.billCode;
	request.custId = cif;

        $.ajax({
        method: "POST",
        url: urlGetOtp,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request),
        }).done(function(response){
        $(".loader_div").hide();
            console.log(response);
            if (response.status == '01') {
                $("#success_tic").modal("show");
              }
        }).fail(function(result) {
            $(".loader_div").hide();
            console.log(result)
        });
}

function callResendOtp() {
    $("#success_tic").modal("hide");
    // $(".loader_div").show();
    if(dataOtp.uuId = undefined || dataOtp.uuId == null) {
        callApiGetOtp();
    }

    var urlGetOtp = "http://localhost:8092/gara/v1.0/sv-get-otp";
    var request = new Object();
	request.uuId = dataOtp.uuId;

        $.ajax({
        method: "POST",
        url: urlGetOtp,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request),
        }).done(function(response){
        // $(".loader_div").hide();
            console.log(response);
        }).fail(function(result) {
            // $(".loader_div").hide();
            console.log(result)
        });
}