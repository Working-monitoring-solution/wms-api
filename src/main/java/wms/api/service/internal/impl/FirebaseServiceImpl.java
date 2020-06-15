package wms.api.service.internal.impl;

import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wms.api.exception.WMSException;
import wms.api.service.internal.FirebaseService;

@Service
@Log4j2
public class FirebaseServiceImpl implements FirebaseService {

    @Autowired
    FirebaseApp firebaseApp;

    @Override
    public String saveImage(MultipartFile file, String id) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Storage storage = bucket.getStorage();
            BlobId blobId = BlobId.of(bucket.getName(), id);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            Blob blob = storage.create(blobInfo, file.getBytes());
            Acl acl = storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            return blob.getMediaLink();
        } catch (NullPointerException e) {
            throw new WMSException.InvalidInputException("image");
        } catch (Exception e) {
            throw new WMSException.UnknownException();
        }
    }

    @Override
    public void pushMessage(String token, String title, String message) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(message)
                    .build();
            Message messageFirebase = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            FirebaseMessaging.getInstance().send(messageFirebase);
        } catch (FirebaseMessagingException e) {
            throw new WMSException.UnknownException();
        }
    }
}
