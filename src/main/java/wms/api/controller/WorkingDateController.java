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

    @RequestMapping(value = "/send-location", method = {RequestMethod.PUT})
    public ResponseEntity sendLocation(@RequestBody SendLocationRequest sendLocationRequest, HttpServletRequest request) {
        return toResult(service.handleLocation(sendLocationRequest, request));
    }

    @RequestMapping(value = "/get-working-date", method = {RequestMethod.GET})
    public void getAllWorkingDateInMonth(@RequestParam String userId,
                                         @RequestParam String month,
                                         @RequestParam String year,
                                         @RequestParam String token,
                                         HttpServletResponse response
    ) {
        service.exportExcelDataInMonth(userId, month, year, token, response);
    }

    @RequestMapping(value = "/create-request", method = {RequestMethod.POST})
    public ResponseEntity createRequest(@RequestParam String date,
                                        @RequestParam String reason,
                                        HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.createRequest(date, reason, request)));
    }

    @RequestMapping(value = "/approve-request", method = {RequestMethod.PUT})
    public ResponseEntity approve(@RequestParam String requestId, HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.approve(requestId, request)));
    }

    @RequestMapping(value = "/deny-request", method = {RequestMethod.PUT})
    public ResponseEntity deny(@RequestParam String requestId, HttpServletRequest request) {
        return toResult(transform.toRequestResponse(service.deny(requestId, request)));
    }

    @RequestMapping(value = "/get-reason", method = {RequestMethod.GET})
    public ResponseEntity getReason() {
        return toResult(service.getReason());
    }

    @RequestMapping(value = "/user/get-pending-request", method = {RequestMethod.GET})
    public ResponseEntity userGetPendingRequest(HttpServletRequest request) {
        return toResult(transform.toListRequestResponse(service.userGetPendingRequest(request)));
    }

    @RequestMapping(value = "/user/get-handled-request", method = {RequestMethod.GET})
    public ResponseEntity userGetHandledRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.userGetHandledRequest(request, page)));
    }

    @RequestMapping(value = "/user/get-handled-request-mobile", method = {RequestMethod.GET})
    public ResponseEntity userGetHandledRequest( HttpServletRequest request) {
        return toResult(transform.toListRequestResponseMobile(service.userGetRequestMobile(request)));
    }

    @RequestMapping(value = "/admin/get-pending-request", method = {RequestMethod.GET})
    public ResponseEntity adminGetPendingRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.adminGetPendingRequest(request, page)));
    }

    @RequestMapping(value = "/admin/count-pending-request", method = {RequestMethod.GET})
    public ResponseEntity adminCountPendingRequest(HttpServletRequest request) {
        return toResult(service.adminCountPendingRequest(request));
    }

    @RequestMapping(value = "/admin/get-handled-request", method = {RequestMethod.GET})
    public ResponseEntity adminGetHandledRequest(@RequestParam String page, HttpServletRequest request) {
        return toResult(transform.toPageRequestResponse(service.adminGetHandledRequest(request, page)));
    }

    @RequestMapping(value = "/mobile/get-info-working-date", method = {RequestMethod.GET})
    public ResponseEntity getInfoMobile(@RequestParam String date, HttpServletRequest request) {
        return toResult(transform.toInfoWorkingDateMobile(service.getInfoWorkingDateMobile(date, request)));
    }

}
