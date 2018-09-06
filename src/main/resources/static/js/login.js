function getLogin(){
    var getEmail =document.getElementById("email-input");
    var getPassword =document.getElementById("password-input");
    var responseAdmin={
        userEmail:getEmail.value,
        password:getPassword.value,
    }

    $.ajax({
        url: "/auth/check",
        contentType: "application/json; charset=utf-8",
        type: "POST",
        data: JSON.stringify(responseAdmin),

        success: function (response) {
         window.location.href="/users";
        },
        error: function (response) {
         window.alert(response.responseText);

        }
    })



}
function Logout(){

    $.ajax({
        url: "/auth/logout",
        type: "GET",


        success: function (data) {
            window.location.href="/login"

        },
        error: function (response) {
            var error = JSON.parse(response.responseText);

            alert(error.message);
        }
    })


}
function goToQualist() {
    window.location.href = "https://www.qualist.com/"

}