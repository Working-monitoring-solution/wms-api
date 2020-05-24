package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.service.internal.ReportMonthService;
import wms.api.transform.ReportTransform;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/report")
public class ReportController extends AbstractController<ReportMonthService, ReportTransform> {

    @PostMapping("/get-report")
    public ResponseEntity getUserReport(@RequestParam String userId,
                                        @RequestParam String month,
                                        @RequestParam String year,
                                        HttpServletRequest request) {
        return toResult(service.getUserReportById(userId, month, year, request));
    }
}
