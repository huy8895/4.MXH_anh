function like(postID, userID) {
    console.log('postID ' + postID)
    console.log('userID ' + userID)
    let likesObj = document.getElementById("likes"+postID+userID) ;
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

$(document).ready(function () {

});

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
        success: function (post) {
            console.log("comment thanh cong +  " + post.id)
        }
    });
    event.preventDefault();
}

function lovePost(id) {
    let heart = document.getElementById("heart-image-" + id)
    if (heart.src.indexOf("active") === -1) {
        heart.src = "tweeter/images/heart-active.svg"
    } else {
        heart.src = "tweeter/images/heart.svg"
    }
}

function deletePost(id) {
    let elementId = "article-container-" + id
    let element = document.getElementById(elementId)
    element.remove()
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

    createPostHTML(postID, userID,contentToPost)
    comment(postID, userID, contentToPost)
    return false;
}

let currentPostId = 1;

function createPostHTML(postID, userID,postContent) {
    let now = new Date()
    let time = now.toLocaleTimeString()
    let date = now.toLocaleString()
    let name = document.getElementById("fullName"+postID+userID).value
    let username = document.getElementById("userName"+postID+userID).value
    currentPostId = currentPostId + 1
    postContent = postContent.replace(/</g, "&lt;")
    postContent = postContent.replace(/\n/g, "<br />")
    postContent = postContent.replace(/(https?:\/\/[^\s]+)/g, "<a href=\"$1\" target=\"_blank\">$1</a>")

    let template = `
				<article id="article-container-${currentPostId}">
					<header>
						<button class="close" onclick="deletePost(${currentPostId})">
							<img src="tweeter/images/close.png" height="15" width="15"/>
						</button>
						<div class="avatar_comment">
						<img src="avatar.png" height="40" width="40"/>
						</div>
						<h1 >${name}</h1>
						<h2 >@${username}</h2>
					</header>
					<blockquote>
						${postContent}
					</blockquote>
					<hr/>
					<footer>
						<p class="date-posted">Posted
							<time>${date}</time>
						</p>
						<button class="heart" onclick="lovePost(${currentPostId})">
							<img src="tweeter/images/heart.svg" id="heart-image-${currentPostId}" height="15" width="16"/>
						</button>
					</footer>
				</article>`
    document.getElementById("form-container").insertAdjacentHTML("afterend", template)
}

