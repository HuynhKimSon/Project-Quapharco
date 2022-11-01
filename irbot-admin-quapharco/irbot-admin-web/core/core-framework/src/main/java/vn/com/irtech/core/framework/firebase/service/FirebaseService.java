package vn.com.irtech.core.framework.firebase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseService {
	public BatchResponse sendNotification(String title, String content, List<String> receiverTokens)
			throws FirebaseMessagingException {
		MulticastMessage message = MulticastMessage.builder()
				.setNotification(Notification.builder().setTitle(title).setBody(content).build())
				.setAndroidConfig(AndroidConfig.builder().setPriority(Priority.HIGH).build())
				.addAllTokens(receiverTokens).build();
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		return response;
	}
  
}
