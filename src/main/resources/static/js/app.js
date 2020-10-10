function like(postID, userID) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    let likesObj = document.getElementById("likes" + postID);
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
        success: function (currentPost) {
            $(likesObj).html(currentPost.likeCount);
        }
    });
    event.preventDefault();
}

function upVote(postID, userID) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    let voteObj = document.getElementById("votes" + postID);
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
        url: "/post/upVote",
        success: function (currentPost) {
            $(voteObj).html(currentPost.voteCount);
        }
    });
    event.preventDefault();
}

function downVote(postID, userID) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    let voteObj = document.getElementById("votes" + postID);
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
        url: "/post/downVote",
        success: function (currentPost) {
            $(voteObj).html(currentPost.voteCount);
        }
    });
    event.preventDefault();
}

function deleteComment(commentId) {
    console.log('commentId ' + commentId)
    let json = {"id": commentId}
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "DELETE",
        data: JSON.stringify(json),
        url: $(location).attr('href'),
        success: function (currentPost) {
            deleteCommentInPost(commentId)
        }
    });
    event.preventDefault();
}


function comment(postID, userID, content) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    console.log('content ' + content)
    let json = {
        "post": {"id": postID},
        "appUser": {"id": userID},
        "content": content
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(json),
        url: $(location).attr('href'),
        success: function (comment) {
            console.log("comment thanh cong +  " + comment.id)
            createCommentInHtml(postID, userID, content, comment.id)
        }
    });
    event.preventDefault();
}

function lovePost(id) {
    let heart = document.getElementById("heart-image-" + id)
    if (heart.src.indexOf("active") === -1) {
        heart.src = "/data/heart-active.svg"
    } else {
        heart.src = "/data/heart.svg"
    }
}

function deleteCommentInPost(commentId) {
    let elementId = "article-container-" + commentId
    let element = document.getElementById(elementId)
    element.remove()
    deleteComment(commentId)
}

function editComment(commentId){

    let elementId = "content-textBoxEdit+" + commentId;
    let element = document.getElementById(elementId);
    let content = document.getElementById(elementId).value
    console.log('commentId'+commentId)
    console.log('content'+content)
    let json = {
        "id":commentId,
        "content":content
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "PUT",
        data: JSON.stringify(json),
        url: $(location).attr('href'),
        success: function (comment) {
            let element = document.getElementById(elementId)
            let template = `
                        ${comment.content}
                        `;
            console.log("comment thanh cong +  " + comment.id)
            document.getElementById(`comment-content${comment.id}`).innerHTML = template;
            console.log("comment thanh cong +  " + comment.id)
        }
    });

}

function editCommentInPost(commentId) {
    let elementId = "comment-content" + commentId
    let element = document.getElementById(elementId)
    let content = document.getElementById(elementId).innerText
    console.log('commentId: '+commentId)
    console.log('postContent: '+content)
    // let template = `<blockquote id="comment-content+${commentId}">
    //                     <form method="post" action="">
    //                     <input type="text" value="${content}" id="content-textBoxEdit+${commentId}"/>
    //                     <button type="button" onclick="editComment(${commentId})">edit</button>
    //                     </form>
	// 				</blockquote>`;
    let template = `<form method="post" action="">
                        <input type="text" value="${content}" id="content-textBoxEdit+${commentId}"/>
                        <button type="button" onclick="editComment(${commentId})">edit</button>
                        </form>`;
    $(element).html(template);
}

function submitComment(postID, userID) {
    let textArea = document.getElementById("input-textarea")
    let counter = document.getElementById("input-characters")
    console.log("click submit" + postID + " " + userID)
    let contentToPost = textArea.value;
    if (contentToPost.length === 0) {
        return false;
    }
    textArea.value = "";
    counter.innerText = 0;

    comment(postID, userID, contentToPost)
    return false;
}

function createNewPost(){
    let form = $('#uploadForm')[0];
    let data = new FormData(form);
    $("#submit-bt").prop("disabled", true);

    $.ajax({
        enctype: 'multipart/form-data',
        type: "POST",
        data: data,
        url: "/post/create",
        // ngăn jQuery tự động chuyển đổi dữ liệu thành chuỗi truy vấn
        processData: false,
        contentType: false,
        success: function (post) {
            console.log(" da dang bai thang cong ")
            let respContent = "<div class=\"alert alert-success alert-dismissible fade show\">\n" +
                "    <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\n" +
                "    <strong>Đăng bài thành công! </strong>ảnh sau khi đăng sẽ xuất hiện trên  <a href=\"#\" class=\"alert-link\">trang bình chọn</a>\n" +
                "  </div>";
            $("#formNewPost_container").html(respContent);
        }
    });
}
function createCommentInHtml(postID, userID, postContent, commentId) {
    let now = new Date()
    let time = now.toLocaleTimeString()
    let date = now.toLocaleString()
    let fullName = document.getElementById("fullName" + postID + userID).value
    let username = document.getElementById("userName" + postID + userID).value
    let avatarFile = document.getElementById("avatarFile" + postID + userID).value
    // postContent = postContent.replace(/</g, "&lt;")
    // postContent = postContent.replace(/\n/g, "<br />")
    // postContent = postContent.replace(/(https?:\/\/[^\s]+)/g, "<a href=\"$1\" target=\"_blank\">$1</a>")

    let template = `
				<article id="article-container-${commentId}">
					<header>
						<button class="close" onclick="deleteCommentInPost(${commentId})">
							<img src="/data/close.png" height="15" width="15"/>
						</button>
						<button class="edit" onclick="editCommentInPost(${commentId})">
                                            edit
                                        </button>
						<div class="avatar_comment">
						<img src="${avatarFile}" height="40" width="40" class="avatar_comment"/>
						</div>
						<h4 >${fullName}</h4>
						<i >@${username}</i>
					</header>
					<blockquote id="comment-content${commentId}">
						${postContent}
					</blockquote>
					<hr/>
					<footer>
						<p class="date-posted">Posted
							<time>${date}</time>
						</p>
						<button class="heart" onclick="loveComment(${commentId},${userID})">
							<img src="/data/heart.svg" id="heart-image-${commentId}" height="15" width="16"/>
						</button>
					</footer>
				</article>`
    document.getElementById("form-container").insertAdjacentHTML("afterend", template)
}
function goDetail(postId) {
    let url = "/post/detail/" + postId;
    window.open(url, "_self");
}

function loveComment(commentId, userId) {
    console.log('commentID ' + commentId);
    console.log('userID ' + userId);
    let heartObj = document.getElementById("heart" + commentId);
    let json = {
        "comment": {"id": commentId},
        "appUser": {"id": userId}
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(json),
        url: "/post/loveComment",
        success: function (currentComment) {
            lovePost(commentId);
            $(heartObj).html(currentComment.loveCount);
        }
    });
    event.preventDefault();
}

