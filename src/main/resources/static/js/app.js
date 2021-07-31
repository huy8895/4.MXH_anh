function like(postID, userID) {
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
            likeStatus(postID);
            $(likesObj).html(currentPost.likeCount);
        }
    });
    event.preventDefault();
}
function likeStatus(postID){
    let like = document.getElementById("LikeButton-" + postID)

    if (like.style.color.indexOf("blue") === -1) {
        like.style.color="blue";
    } else {
        like.style.color="gray";
    }
}
//=============================================================================================


function upVote(postID, userID) {
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
            if(currentPost.voteCount>=4) {
                let currentPostHtml = document.getElementById("postCurrent-"+postID);
                let respContent = "<div class=\"alert alert-primary alert-dismissible fade show\">\n" +
                    "    <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\n" +
                    "    <strong>UpVote thành công! </strong>Chúc Mừng Ảnh đã được lên <a href=\"home\" class=\"alert-lik\">Trang Chủ</a>\n" +
                    "  </div>";
                currentPostHtml.innerHTML = respContent;
            }
            else
                $(voteObj).html(currentPost.voteCount);
        }
    });
    event.preventDefault();
}
function downVote(postID, userID) {
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


//=============================================================================================
function deleteComment(commentId) {
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
            document.getElementById(`comment-content${comment.id}`).innerHTML = template;
        }
    });

}
function editCommentInPost(commentId) {
    let elementId = "comment-content" + commentId
    let element = document.getElementById(elementId)
    let content = document.getElementById(elementId).innerText
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
    let contentToPost = textArea.value;
    textArea.value = "";
    counter.innerText = 0;
    if (contentToPost.length != 0) {
        comment(postID, userID, contentToPost)
    }
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
<!--							<img src="/data/heart.svg" id="heart-image-${commentId}" height="15" width="16"/>-->
							<i class='fa fa-heart' style='font-size: 1.2em;color:darkgrey'
                                   id="heart-image-${commentId}"></i>
						</button>
					</footer>
				</article>`
    document.getElementById("form-container").insertAdjacentHTML("afterend", template)
}
function comment(postID, userID, content) {
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
            createCommentInHtml(postID, userID, content, comment.id)
        }
    });
    event.preventDefault();
}

function lovePost(id) {
    let heart = document.getElementById("heart-image-" + id)
    if (heart.style.color.indexOf("deeppink") === -1) {
        heart.style.color="deeppink";
    } else {
        heart.style.color="darkgrey";
    }
}
function loveComment(commentId, userId) {
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
            lovePost(currentComment.id);
            $(heartObj).html(currentComment.loveCount);
        }
    });
    event.preventDefault();
}

//=============================================================================================================================================================
function createNewPost(){
    let form = $('#uploadForm')[0];
    let data = new FormData(form);
    let newPostContent = document.getElementById("newPostContent").value;
    if(newPostContent.length !== 0){
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
                let respContent = "<div class=\"alert alert-success alert-dismissible fade show mt-4\">\n" +
                    "    <button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\n" +
                    "    <strong>Đăng bài thành công! </strong>ảnh sau khi đăng sẽ xuất hiện trên  <a href=\"vote\" class=\"alert-link\">trang bình chọn</a>\n" +
                    "  </div>";
                $("#formNewPost_container").html(respContent);
            }
        });
    } else {
        alert("bạn chưa nhập nội dung của Post ")
    }

}
function deletePost(postId) {
    let postHtml = document.getElementById("postPre" + postId);
    let json = {"id": postId};
    let currentPostHTML = document.getElementById("postCurrent-"+postId);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "DELETE",
        data: JSON.stringify(json),
        url: "/post/delete",
        success: function (currentPost) {
            currentPostHTML.remove();
        }
    });
    event.preventDefault();
}
function goDetail(postId) {
    let url = "/post/detail/" + postId;
    window.open(url, "_self");
}

//=============================================================================================


function blockUser(userId){
    let json = {"id": userId};
    let template = document.getElementById("role-"+userId);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(json),
        url: "/admin/block",
        success: function (currentComment) {
            template.innerHTML = "ROLE_BLOCKED";
        }
    });
    event.preventDefault();
}