package wms.api.service.internal;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseService {
    String saveImage(MultipartFile file, String id);
    void pushMessage(String token, String title, String message);
}
