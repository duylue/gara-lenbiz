$(document).ready(function () {
    $("#identity").change(function() {
        previewFile($("#front_identity"), $("#identity"))
    })   

    $("#backIdentity").change(function() {
        previewFile($("#back_identity"), $("#backIdentity"))
    })   

    $("#input-selfie").change(function() {
        previewFile($("#selfie"), $("#input-selfie"))
    })   

    $("#upload-kyc").click(function(){
        verifyIdentity();
    })
});

function previewFile(img, input) {
    const preview = img;
    const file = input[0].files[0];
    const reader = new FileReader();
  
    reader.addEventListener("load", function () {
      // convert image file to base64 string
      preview.attr("src", reader.result);
    }, false);
  
    if (file) {
      reader.readAsDataURL(file);
    }
  }

  function verifyIdentity() {
    var cif = getUrlParameter("cif");
    var urlUpload = "http://localhost:8092/gara/v1.0/ekyc?key=E-KYC&cif=" + cif;

        var fd = new FormData();
        var frontIdentity = $('#identity')[0].files;
        var backIdentity = $('#backIdentity')[0].files;
        var selfie = $('#input-selfie')[0].files;

        // Check file selected or not
        if(frontIdentity.length > 0 && backIdentity.length > 0 && selfie.length > 0){
            $("#success_tic").modal("hide");
            $(".loader_div").show();

           fd.append('file',frontIdentity[0]);
           fd.append('file',backIdentity[0]);
           fd.append('file',selfie[0]);

           $.ajax({
            method: "POST",
            url: urlUpload,
            contentType: false,
            processData: false,
            data: fd,
          }).done(function(response){
            $(".loader_div").hide();
              console.log(response);
              if (response.status == '01') {
                $("#success_tic").modal("show");
                setTimeout(function(){
                    $("#success_tic").modal("hide");
                    window.location.href = "http://localhost:8092/ky-hop-dong?cif="+ cif;  
                }, 3000);
              }
            })
            .fail(function(result) {
                $(".loader_div").hide();
                console.log(result)
            });
        }else{
           alert("Please select a file.");
        }
  }
