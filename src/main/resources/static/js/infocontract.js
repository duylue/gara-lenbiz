$(document).ready(function () {
    $("#accept").click(function() {
        if ($("#ischeck")[0].checked) {
            var cif = getUrlParameter("cif");
            window.location.href = "http://localhost:8092/xac-minh-danh-tinh?cif="+ cif;
        }
    })
});
