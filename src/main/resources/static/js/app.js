
    function like(postID, userID) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    let json = {
    "post": {"id": postID},
    "appUser": {"id": userID}
}
    $.ajax({
    headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
},
    type: "POST",
    data: JSON.stringify(json),
    url: "/post/like",
    success: function (ms) {
    console.log("like thang cong " + ms)
}
});
    event.preventDefault();
}

    $(document).ready(function () {

});


