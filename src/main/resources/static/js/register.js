$(document).ready(function () {
    $("#register").click(function() {
        register();
    })
});

function register() {
    $('#success_tic').modal('hide');
    $(".loader_div").show();

    var request = buildData();
    console.log(request);
    
    $.ajax({
        method: "POST",
        url: "http://localhost:8092/gara/v1.0/register",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request),
      }).done(function(response){
        $(".loader_div").hide();
              console.log(response);
              if (response.status == '01') {
                $("#success_tic").modal("show");
                setTimeout(function(){
                    $("#success_tic").modal("hide");
                    window.location.href = "http://localhost:8092/tai-thong-tin?cif="+ response.data;  
                }, 3000);
              }
        })
        .fail(function(result) {
            $(".loader_div").hide();
            console.log(result)
        });
}

function buildData() {
    var request = new Object();
    request.idCode = $("#idCode").val();
    request.idDate = $("#idDate").val();
    request.revenue = $("#revenue").val();

    request.fullName = $("#fullName").val();
    request.dateOfBirth = $("#dateOfBirth").val();
	 request.refIdCode = $("#refIdCode").val();
	 request.refIdDate = $("#refIdDate").val();
	 request.refIdPlace = $("#refIdPlace").val();

	 request.address = $("#address").val();
	 request.relationAddress = $("#relationAddress").val();
	 request.mobileSms = $("#mobileSms").val();

	 request.email = $("#email").val();
	 request.refName = $("#refName").val();
	 request.phone = $("#phone").val();

	 request.mrLoanLimit = $("#mrLoanLimit").val();
     request.time2rc = $("#time2rc").val();
     
     return request;
}