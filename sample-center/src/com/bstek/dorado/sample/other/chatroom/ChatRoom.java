package com.bstek.dorado.sample.other.chatroom;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.view.View;

@Component
public class ChatRoom {
	private static MessageBase messageBase = new MessageBase();
	private static int SerialNo = 1;
	private static final int MAX_RETRIEVING_TIMES = 60;

	public synchronized void onReady(View view) {
		view.setUserData("Dorado User" + (SerialNo++));
	}

	private List<Message> tryGetMessages(long lastMessageId) {
		List<Message> messages = messageBase.getMessages();
		if (lastMessageId > 0) {
			List<Message> allMessages = messages;
			messages = null;
			int sz = allMessages.size();
			for (int i = sz - 1; i >= 0; i--) {
				Message message = allMessages.get(i);
				if (message.getId() <= lastMessageId) {
					messages = allMessages.subList(i + 1, sz);
					break;
				}
			}
		}
		if (messages != null && messages.isEmpty()) {
			messages = null;
		}
		return messages;
	}

	@Expose
	public List<Message> getMessages(long lastMessageId)
			throws InterruptedException {
		List<Message> messages = null;
		int i = 0;
		while (i < MAX_RETRIEVING_TIMES) {
			i++;
			messages = tryGetMessages(lastMessageId);
			if (messages != null) {
				break;
			}
			Thread.sleep(500L);
		}
		return messages;
	}

	@Expose
	public void sendMessage(String user, String message) {
		messageBase.pushMessage(user, message);
	}
}

class MessageBase {
	public static final int MAX_MESSAGE_COUNT = 100;

	private List<Message> messages = new Vector<Message>(MAX_MESSAGE_COUNT);

	public MessageBase() {
		pushMessage("System", "欢迎来到Dorado聊天室！");
	}

	public List<Message> getMessages() {
		return Collections.unmodifiableList(messages);
	}

	public synchronized void pushMessage(String user, String message) {
		if (messages.size() > MAX_MESSAGE_COUNT - 1) {
			int sz = messages.size(), gap = sz - MAX_MESSAGE_COUNT - 1;
			for (int i = 0; i < gap; i++) {
				messages.remove(0);
			}
		}

		Message m = new Message();
		m.setUser(user);
		m.setText(message);
		m.setTime(new Date());
		messages.add(m);
	}
}

class Message {
	private long id;
	private String user;
	private String text;
	private Date time;

	public Message() {
		id = System.currentTimeMillis();
	}

	public long getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
