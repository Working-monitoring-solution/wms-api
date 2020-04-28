package wms.api.service.internal;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseService {
    String saveImage(MultipartFile file, String id);
}
