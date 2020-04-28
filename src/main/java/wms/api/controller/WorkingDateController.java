package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wms.api.common.request.SendLocationRequest;

import java.util.Date;

@RestController
@RequestMapping("/api/working-date")
public class WorkingDateController {

    @PostMapping("/send-location")
    public ResponseEntity sendLocation(@RequestBody SendLocationRequest sendLocationRequest) {
        return null;
    }
}
