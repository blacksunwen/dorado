var currentUser, lastMessageId = 0;

// @Bind view.onReady
!function(inputUser) {
	currentUser = view.get("userData");
	inputUser.set("text", currentUser);
}

// @Bind #buttonEnter.onClick
!function(inputUser, cardbookChatRoom, panelChatRoom, retrieveMessageAction) {
	var user = inputUser.get("text");
	if (!user) {
		throw new dorado.Exception("请输入一个有效的昵称！");
	}

	currentUser = user;
	cardbookChatRoom.set("currentIndex", 1);
	panelChatRoom.set("caption", "Chat Room - " + user);
	retrieveMessageAction.execute();
}

// @Bind #inputMessage.onTextEdit
!function(self, sendMessageAction) {
	sendMessageAction.set("disabled", self.get("text") == "");
}

// @Bind #sendMessageAction.beforeExecute
!function(self, inputMessage) {
	self.set("parameter", {
		user : currentUser,
		message : inputMessage.get("text")
	});
	inputMessage.set("text", "");
	self.set("disabled", true);
}

// @Bind #retrieveMessageAction.beforeExecute
!function(self) {
	self.set("parameter", lastMessageId);
}

function formatDate(time) {
	var date = Date.parseDate(time, "Y-m-d\\TH:i:s\\Z");
	return date.formatDate("n-d H:i:s");
}

// @Bind #retrieveMessageAction.onSuccess
!function(self, arg) {
	var messagesDiv = view.get("#messageList").getDom();
	if (arg.result) {
		arg.result.each(function(message) {
			$(messagesDiv).xCreate([ {
				tagName : "DIV",
				className : "message-author",
				contentText : message.user + " - " + formatDate(message.time)
			}, {
				tagName : "DIV",
				className : "message-text",
				contentText : message.text
			} ]);
			messagesDiv.scrollTop = messagesDiv.scrollHeight
					- messagesDiv.clientHeight;

			if (message.id > lastMessageId) {
				lastMessageId = message.id;
			}
		});
	}
	self.execute();
}