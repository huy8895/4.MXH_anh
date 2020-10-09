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
            $(likesObj).innerHTML(currentPost.likeCount);
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
            $(voteObj).innerHTML(currentPost.voteCount);
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
            createPostHTML(postID, userID, content, comment.id)
        }
    });
    event.preventDefault();
}

function deletePost(id, commentId) {
    let elementId = "article-container-" + id
    let element = document.getElementById(elementId)
    element.remove()
    deleteComment(commentId)
}


function submitPost(postID, userID) {
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


function createPostHTML(postID, userID, postContent, commentId) {
    let now = new Date();
    let time = now.toLocaleTimeString();
    let date = now.toLocaleString();
    let fullName = document.getElementById("fullName" + postID + userID).value;
    let username = document.getElementById("userName" + postID + userID).value;
    let avatarFile = document.getElementById("avatarFile" + postID + userID).value;
    // postContent = postContent.replace(/</g, "&lt;");
    // postContent = postContent.replace(/\n/g, "<br />");
    // postContent = postContent.replace(/(https?:\/\/[^\s]+)/g, "<a href=\"$1\" target=\"_blank\">$1</a>");

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
						<h1 >${fullName}</h1>
						<h2 >@${username}</h2>
					</header>
					<blockquote id="comment-content${commentId}">
						${postContent}
					</blockquote>
					<hr/>
					<footer>
						<p class="date-posted">Posted
							<time>${date}</time>
						</p>
                            <button class="heart"
                                    th:onclick="'loveComment(\''+ ${commentId} +'\',\'' + ${commentId} +'\',\''+${userID} + '\');'">
                                <img src="/data/heart.svg" id="heart-image-${commentId}" height="15" width="16"/>
                                <span
                                        class="heart" th:id="'heart' + ${comment.id}" title="heart"
                                        th:text="  ${comment.loveCount}"></span>
                            </button>
					</footer>
				</article>`
    document.getElementById("form-container").insertAdjacentHTML("afterend", template)
}

function goDetail(postId) {
    let url = "/post/detail/" + postId;
    window.open(url, "_self");
}

function lovePost(id) {
    let heart = document.getElementById("heart-image-" + id)
    if (heart.src.indexOf("active") === -1) {
        heart.src = "/data/heart-active.svg"
    } else {
        heart.src = "/data/heart.svg"
    }
}

function loveComment(idPass, commentId, userId) {
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
            lovePost(idPass);
            $(heartObj).innerHTML(currentComment.loveCount);
        }
    });
    event.preventDefault();
}

