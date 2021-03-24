$(document).ready(function () {
    $("#upload-dkkd").change(function() {
        var filename = $('#upload-dkkd').val().split('\\').pop();
        $("#image-dkkd").text(filename);
    })

    $("#upload-cmnd").change(function() {
        var filename = $('#upload-cmnd').val().split('\\').pop();
        $("#image-cmnd").text(filename);
    })

    $("#upload-shk").change(function() {
        var filename = $('#upload-shk').val().split('\\').pop();
        $("#image-shk").text(filename);
    })

    $("#upload-sim").change(function() {
        var filename = $('#upload-sim').val().split('\\').pop();
        $("#image-sim").text(filename);
    })

    $("#btn-upfile").click(function(){
        var cif = getUrlParameter("cif");
        
        var hsbt = $("#maHsbt").val().trim();
        var urlUpload = "http://localhost:8092/gara/v1.0/upload?key=HSPL&cif=" + cif + "&hsbt=" + hsbt;

        var fd = new FormData();
        var dkkd = $('#upload-dkkd')[0].files;
        var cmnd = $('#upload-cmnd')[0].files;
        var shk = $('#upload-shk')[0].files;
        var sim = $('#upload-sim')[0].files;

        // Check file selected or not
        if(dkkd.length > 0 && cmnd.length > 0 && shk.length > 0 && sim.length > 0 ){
            $("#success_tic").modal("hide");
            $(".loader_div").show();

           fd.append('file',dkkd[0]);
           fd.append('file',cmnd[0]);
           fd.append('file',shk[0]);
           fd.append('file',sim[0]);

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
                    window.location.href = "http://localhost:8092/thong-tin-hop-dong?cif="+ cif;  
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
    });
});

