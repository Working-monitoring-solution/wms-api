package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.SendLocationRequest;
import wms.api.service.internal.WorkingDateService;
import wms.api.transform.Transform;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/working-date")
public class WorkingDateController extends AbstractController<WorkingDateService, Transform>{

    @PostMapping("/send-location")
    public ResponseEntity sendLocation(@RequestBody SendLocationRequest sendLocationRequest, HttpServletRequest request) {
        return toResult(service.handleLocation(sendLocationRequest, request));
    }
}
