package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.SendLocationRequest;
import wms.api.service.internal.WorkingDateService;
import wms.api.transform.WorkingDateTransform;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/working-date")
public class WorkingDateController extends AbstractController<WorkingDateService, WorkingDateTransform> {

    @PostMapping("/send-location")
    public ResponseEntity sendLocation(@RequestBody SendLocationRequest sendLocationRequest, HttpServletRequest request) {
        return toResult(service.handleLocation(sendLocationRequest, request));
    }

    @RequestMapping(value = "/get-working-date", method = {RequestMethod.POST, RequestMethod.GET})
    public void getAllWorkingDateInMonth(@RequestParam String userId,
                                         @RequestParam String month,
                                         @RequestParam String year,
                                         @RequestParam String token,
                                         HttpServletResponse response
    ) {
        service.exportExcelDataInMonth(userId, month, year, token, response);
    }

    @GetMapping(value = "/create-request")
    public ResponseEntity createRequest(@RequestParam String date,
                                        @RequestParam String reason,
                                        HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.createRequest(date, reason, request)));
    }

    @GetMapping(value = "/approve-request")
    public ResponseEntity approve(@RequestParam String requestId, HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.approve(requestId, request)));
    }

    @GetMapping(value = "/deny-request")
    public ResponseEntity deny(@RequestParam String requestId, HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.deny(requestId, request)));
    }

    @GetMapping(value = "/get-reason")
    public ResponseEntity getReason() {
        return toResult(service.getReason());
    }

    @GetMapping(value = "/user/get-pending-request")
    public ResponseEntity userGetPendingRequest(HttpServletRequest request) {
        return toResult(transform.toListRequestResponse(service.userGetPendingRequest(request)));
    }

    @GetMapping(value = "/user/get-handled-request")
    public ResponseEntity userGetHandledRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.userGetHandledRequest(request, page)));
    }

    @GetMapping(value = "/admin/get-pending-request")
    public ResponseEntity adminGetPendingRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.adminGetPendingRequest(request, page)));
    }

    @GetMapping(value = "/admin/count-pending-request")
    public ResponseEntity adminCountPendingRequest(HttpServletRequest request) {
        return toResult(service.adminCountPendingRequest(request));
    }

    @GetMapping(value = "/admin/get-handled-request")
    public ResponseEntity adminGetHandledRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.adminGetHandledRequest(request, page)));
    }
}
