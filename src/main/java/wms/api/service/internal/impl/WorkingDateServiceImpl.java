package wms.api.service.internal.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wms.api.common.request.SendLocationRequest;
import wms.api.constant.WMSConstant;
import wms.api.dao.entity.Request;
import wms.api.dao.entity.User;
import wms.api.dao.entity.WorkingDate;
import wms.api.dao.repo.RequestRepository;
import wms.api.dao.repo.UserRepository;
import wms.api.dao.repo.WorkingDateRepository;
import wms.api.exception.WMSException;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.FirebaseService;
import wms.api.service.internal.WorkingDateService;
import wms.api.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WorkingDateServiceImpl extends BaseServiceImpl<WorkingDateRepository, WorkingDate, Long> implements WorkingDateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    FirebaseService firebaseService;

    @Override
    public WorkingDate handleLocation(SendLocationRequest sendLocationRequest, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);
        Date date = Utils.toBeginDate(new Date());
        WorkingDate workingDate = repo.getWorkingDateByUserAndDateAndPermissionIsFalse(user, date);
        if (ObjectUtils.isEmpty(workingDate)) {
            throw new WMSException.NotFoundEntityException("working date");
        }
        return updateWorkingDate(sendLocationRequest, workingDate);
    }

    private List<WorkingDate> getAllWorkingDateInMonth(String userId, String month, String year, String token) {
        User manager = tokenService.validateAdminToken(token);
        User user = userRepository.findById(Utils.toLong(userId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("user")
        );
        if (!ObjectUtils.isEmpty(user.getManager()) && !user.getManager().equals(manager) && !ObjectUtils.isEmpty(manager.getManager())) {
            throw new WMSException.AuthorizationErrorException();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Utils.toInt(month, "month")-1);
        calendar.set(Calendar.YEAR, Utils.toInt(year, "year"));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        Date toDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, WMSConstant.BEGIN_DATE_OF_MONTH);
        Utils.toBeginDate(calendar);
        Date fromDate = calendar.getTime();
        return repo.getByUserAndDateBetween(user, fromDate, toDate);
    }

    @Override
    public void exportExcelDataInMonth(String userId, String month, String year, String token, HttpServletResponse response) {
        try {
            List<WorkingDate> workingDateList = getAllWorkingDateInMonth(userId, month, year, token);
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream("report.xls");
            Sheet sheet = workbook.createSheet(month + "-" + year);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Times New Roman");
            style.setFillForegroundColor(IndexedColors.WHITE.index);
            style.setFillPattern(FillPatternType.forInt((short) 1));
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.index);
            style.setFont(font);
            style.setBorderBottom(BorderStyle.valueOf((short) 1));
            style.setBorderTop(BorderStyle.valueOf((short) 1));
            style.setBorderRight(BorderStyle.valueOf((short) 1));
            style.setBorderBottom(BorderStyle.valueOf((short) 1));
            style.setAlignment(HorizontalAlignment.CENTER);

            // set row title
            String[] title = {"Date", "User id", "Check in", "Check out", "Permission", "08:00", "08:15", "08:30",
                    "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15",
                    "11:30", "11:45", "12:00", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
                    "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00"};
            Row header = sheet.createRow(0);
            for (int col = 0; col < title.length; col++) {
                Cell cell = header.createCell(col);
                cell.setCellValue(title[col]);
                cell.setCellStyle(style);
            }

            CellStyle falseCellStyle = workbook.createCellStyle();
            Font falseFont = workbook.createFont();
            falseFont.setColor(IndexedColors.RED.index);
            falseFont.setFontName("Times New Roman");
            falseCellStyle.setFont(falseFont);

            // set data
            int rowIdx = 1;
            for (WorkingDate workingDate : workingDateList) {
                Row row = sheet.createRow(rowIdx++);
                DateFormat dateFormat = new SimpleDateFormat(Utils.ddMMyyyy);
                row.createCell(0).setCellValue(dateFormat.format(workingDate.getDate()));
                row.createCell(1).setCellValue(workingDate.getUser().getId());
                if (workingDate.getCheckIn() != null) {

                    row.createCell(2).setCellValue(workingDate.getCheckIn().toString());
                }
                if (workingDate.getCheckOut() != null) {
                    row.createCell(3).setCellValue(workingDate.getCheckOut().toString());
                }
                row.createCell(4).setCellValue(workingDate.isPermission());
                updateCell(row, 5, falseCellStyle, workingDate.isAt0800());
                updateCell(row, 6, falseCellStyle, workingDate.isAt0815());
                updateCell(row, 7, falseCellStyle, workingDate.isAt0830());
                updateCell(row, 8, falseCellStyle, workingDate.isAt0845());
                updateCell(row, 9, falseCellStyle, workingDate.isAt0900());
                updateCell(row, 10, falseCellStyle, workingDate.isAt0915());
                updateCell(row, 11, falseCellStyle, workingDate.isAt0930());
                updateCell(row, 12, falseCellStyle, workingDate.isAt0945());
                updateCell(row, 13, falseCellStyle, workingDate.isAt1000());
                updateCell(row, 14, falseCellStyle, workingDate.isAt1015());
                updateCell(row, 15, falseCellStyle, workingDate.isAt1030());
                updateCell(row, 16, falseCellStyle, workingDate.isAt1045());
                updateCell(row, 17, falseCellStyle, workingDate.isAt1100());
                updateCell(row, 18, falseCellStyle, workingDate.isAt1115());
                updateCell(row, 19, falseCellStyle, workingDate.isAt1130());
                updateCell(row, 20, falseCellStyle, workingDate.isAt1145());
                updateCell(row, 21, falseCellStyle, workingDate.isAt1200());
                updateCell(row, 22, falseCellStyle, workingDate.isAt1300());
                updateCell(row, 23, falseCellStyle, workingDate.isAt1315());
                updateCell(row, 24, falseCellStyle, workingDate.isAt1330());
                updateCell(row, 25, falseCellStyle, workingDate.isAt1345());
                updateCell(row, 26, falseCellStyle, workingDate.isAt1400());
                updateCell(row, 27, falseCellStyle, workingDate.isAt1415());
                updateCell(row, 28, falseCellStyle, workingDate.isAt1430());
                updateCell(row, 29, falseCellStyle, workingDate.isAt1445());
                updateCell(row, 30, falseCellStyle, workingDate.isAt1500());
                updateCell(row, 31, falseCellStyle, workingDate.isAt1515());
                updateCell(row, 32, falseCellStyle, workingDate.isAt1530());
                updateCell(row, 33, falseCellStyle, workingDate.isAt1545());
                updateCell(row, 34, falseCellStyle, workingDate.isAt1600());
                updateCell(row, 35, falseCellStyle, workingDate.isAt1615());
                updateCell(row, 36, falseCellStyle, workingDate.isAt1630());
                updateCell(row, 37, falseCellStyle, workingDate.isAt1645());
                updateCell(row, 38, falseCellStyle, workingDate.isAt1700());
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.setVerticallyCenter(true);

            // export
            String fileName = "report_" + month + "/" + year;
            workbook.write(fos);
            fos.close();
            response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            response.setContentType("application/vnd.ms-excel");
            IOUtils.copy(new FileInputStream("report.xls"), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new WMSException.UnknownException();
        }
    }

    @Override
    public Request createRequest(String date, String reason, HttpServletRequest httpServletRequest) {
        User user = tokenService.validateUserToken(getTokenFromHeader(httpServletRequest));
        Date requestDate = Utils.toDate(date, Utils.ddMMyyyy);
        if (requestRepository.existsByUserAndDate(user, requestDate)) {
            throw new WMSException.DuplicateEntityException("request date");
        }
        Request request = Request.builder()
                .date(requestDate)
                .user(user)
                .manager(user.getManager())
                .status(Request.STATUS_PENDING)
                .reason(reason)
                .build();
        request = requestRepository.save(request);
        return request;
    }

    @Override
    public Request approve(String requestId, HttpServletRequest httpServletRequest) {
        User manager = tokenService.validateAdminToken(getTokenFromHeader(httpServletRequest));
        Request request = getRequestById(requestId, manager);
        request.setStatus(Request.STATUS_APPROVED);
        WorkingDate workingDate = repo.getByDateAndUser(request.getDate(), request.getUser()).orElseThrow(WMSException.NotFoundEntityException::new);
        if (ObjectUtils.isEmpty(workingDate)) {
            throw new WMSException.NotFoundEntityException("working date");
        }
        workingDate.setPermission(true);
        request = requestRepository.save(request);
        repo.save(workingDate);
        firebaseService.pushMessage(request.getUser().getDeviceToken(), "Request approved", "Manager approved your request");
        return request;
    }

    @Override
    public Request deny(String requestId, HttpServletRequest httpServletRequest) {
        User manager = tokenService.validateAdminToken(getTokenFromHeader(httpServletRequest));
        Request request = getRequestById(requestId, manager);
        request.setStatus(Request.STATUS_DENIED);
        request = requestRepository.save(request);
        firebaseService.pushMessage(request.getUser().getDeviceToken(), "Request denied", "Manager denied your request");
        return request;
    }

    @Override
    public List<String> getReason() {
        List<String> reason = new ArrayList<>();
        reason.add("I need a sick leave");
        reason.add("I need to see a doctor");
        reason.add("My wife’s labor");
        reason.add("I have a business travel");
        reason.add("I have unexpected reasons");
        reason.add("I have personal reasons");
        reason.add("Other");
        return reason;
    }

    @Override
    public List<Request> userGetPendingRequest(HttpServletRequest request) {
        User user = tokenService.validateUserToken(getTokenFromHeader(request));
        return requestRepository.getByUserAndStatus(user, Request.STATUS_PENDING);
    }

    @Override
    public Page<Request> userGetHandledRequest(HttpServletRequest request, String page) {
        User user = tokenService.validateUserToken(getTokenFromHeader(request));
        Pageable pageable = PageRequest.of(Utils.toInt(page, "page"),
                WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").descending());
        return requestRepository.getByUserAndStatusNotLike(user, Request.STATUS_PENDING, pageable);
    }

    @Override
    public List<Request> userGetRequestMobile(HttpServletRequest request) {
        User user = tokenService.validateUserToken(getTokenFromHeader(request));
        return requestRepository.getByUser(user);
    }

    @Override
    public Page<Request> adminGetPendingRequest(HttpServletRequest request, String page) {
        User manager = tokenService.validateAdminToken(getTokenFromHeader(request));
        Pageable pageable = PageRequest.of(Utils.toInt(page, "page"),
                WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").ascending());
        if (ObjectUtils.isEmpty(manager.getManager())) {
            return requestRepository.getByStatus(Request.STATUS_PENDING, pageable);
        }
        return requestRepository.getByManagerAndStatus(manager, Request.STATUS_PENDING, pageable);
    }

    @Override
    public Long adminCountPendingRequest(HttpServletRequest request) {
        User manager = tokenService.validateAdminToken(getTokenFromHeader(request));
        if (ObjectUtils.isEmpty(manager.getManager())) {
            return requestRepository.countAllByStatus(Request.STATUS_PENDING);
        }
        return requestRepository.countAllByManagerAndStatus(manager, Request.STATUS_PENDING);
    }

    @Override
    public Page<Request> adminGetHandledRequest(HttpServletRequest request, String page) {
        User manager = tokenService.validateAdminToken(getTokenFromHeader(request));
        Pageable pageable = PageRequest.of(Utils.toInt(page, "page"),
                WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").descending());
        if (ObjectUtils.isEmpty(manager.getManager())) {
            return requestRepository.getByStatusNotLike(Request.STATUS_PENDING, pageable);
        }
        return requestRepository.getByManagerAndStatusNotLike(manager, Request.STATUS_PENDING, pageable);
    }

    @Override
    public WorkingDate getInfoWorkingDateMobile(String date, HttpServletRequest request) {
        User user = tokenService.validateUserToken(getTokenFromHeader(request));
        Date dateFormat;
        try {
            dateFormat = new SimpleDateFormat(Utils.ddMMyyyy).parse(date);
            String dateString = new SimpleDateFormat(Utils.DATE_TIME_MYSQL).format(dateFormat);
            Date newDate = Utils.toDate(dateString, Utils.DATE_TIME_MYSQL);
            WorkingDate workingDate = repo.getByDateAndUser(newDate, user).orElseThrow(WMSException.NotFoundEntityException::new);
            return workingDate;
        } catch (ParseException e) {
            throw new WMSException.InvalidInputException();
        }
    }

    private Request getRequestById(String requestId, User manager) {
        Request request = requestRepository.findById(Utils.toLong(requestId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("request")
        );
        if (!ObjectUtils.isEmpty(manager.getManager()) && !request.getManager().equals(manager)) {
            throw new WMSException.AuthorizationErrorException();
        }
        return request;
    }

    private WorkingDate updateWorkingDate(SendLocationRequest request, WorkingDate workingDate) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean checkValue = checkLocation(request);
        return update(hour, minute, checkValue, workingDate);
    }

    // check if location is in the company
    private boolean checkLocation(SendLocationRequest request) {
        double distance = getDistance(Utils.toDouble(request.getLatitude()), Utils.toDouble(request.getLongitude()));
        return !(distance > WMSConstant.ALLOW_DISTANCE);
    }

    // get distance (meters) from location to company coordinates
    private double getDistance(double lat, double lon) {
        double latDistance = Math.toRadians(WMSConstant.LAT - lat);
        double lonDistance = Math.toRadians(WMSConstant.LONG - lon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(WMSConstant.LAT))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return WMSConstant.RADIUS * c * 1000;
    }

    // from current hour and minute get corresponding field in table and write value
    private WorkingDate update(int hour, int minute, boolean checkValue, WorkingDate workingDate) {
        if ((hour < 8) || (hour == 8 && minute < 9)) {
            workingDate.setAt0800(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 8 && minute > 12 && minute < 24) {
            workingDate.setAt0815(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0800() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 8 && minute > 27 && minute < 39) {
            workingDate.setAt0830(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0815() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 8 && minute > 42 && minute < 54) {
            workingDate.setAt0845(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0830() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 8 && minute > 57) || (hour == 9 && minute < 9)) {
            workingDate.setAt0900(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0845() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 9 && minute > 12 && minute < 24) {
            workingDate.setAt0915(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0900() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 9 && minute > 27 && minute < 39) {
            workingDate.setAt0930(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0915() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 9 && minute > 42 && minute < 54) {
            workingDate.setAt0945(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0930() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 9 && minute > 57) || (hour == 10 && minute < 9)) {
            workingDate.setAt1000(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt0945() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 10 && minute > 12 && minute < 24) {
            workingDate.setAt1015(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1000() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 10 && minute > 27 && minute < 39) {
            workingDate.setAt1030(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1015() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 10 && minute > 42 && minute < 54) {
            workingDate.setAt1045(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1030() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 10 && minute > 57) || (hour == 11 && minute < 9)) {
            workingDate.setAt1100(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1045() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 11 && minute > 12 && minute < 24) {
            workingDate.setAt1115(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1100() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 11 && minute > 27 && minute < 39) {
            workingDate.setAt1130(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1115() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 11 && minute > 42 && minute < 54) {
            workingDate.setAt1145(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1130() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 11 && minute > 57) || (hour == 12 && minute < 9)) {
            workingDate.setAt1200(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1145() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 12 && minute > 57) || (hour == 13 && minute < 9)) {
            workingDate.setAt1300(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1200() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 13 && minute > 12 && minute < 24) {
            workingDate.setAt1315(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1300() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 13 && minute > 27 && minute < 39) {
            workingDate.setAt1330(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1315() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 13 && minute > 42 && minute < 54) {
            workingDate.setAt1345(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1330() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 13 && minute > 57) || (hour == 14 && minute < 9)) {
            workingDate.setAt1400(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1345() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 14 && minute > 12 && minute < 24) {
            workingDate.setAt1415(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1400() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 14 && minute > 27 && minute < 39) {
            workingDate.setAt1430(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1415() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 14 && minute > 42 && minute < 54) {
            workingDate.setAt1445(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1430() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 14 && minute > 57) || (hour == 15 && minute < 9)) {
            workingDate.setAt1500(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1445() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 15 && minute > 12 && minute < 24) {
            workingDate.setAt1515(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1500() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 15 && minute > 27 && minute < 39) {
            workingDate.setAt1530(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1515() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 15 && minute > 42 && minute < 54) {
            workingDate.setAt1545(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1530() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 15 && minute > 57) || (hour == 16 && minute < 9)) {
            workingDate.setAt1600(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1545() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 16 && minute > 12 && minute < 24) {
            workingDate.setAt1615(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1600() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 16 && minute > 27 && minute < 39) {
            workingDate.setAt1630(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1615() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if (hour == 16 && minute > 42 && minute < 54) {
            workingDate.setAt1645(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            if (workingDate.isAt1630() && !checkValue) {
                workingDate.setComeOut(true);
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        if ((hour == 16 && minute > 57) || (hour == 17 && minute < 9)) {
            workingDate.setAt1700(checkValue);
            if (ObjectUtils.isEmpty(workingDate.getCheckIn()) && checkValue) {
                workingDate.setCheckIn(new Timestamp(new Date().getTime()));
            }
            workingDate.setCheckOut(new Timestamp(new Date().getTime()));
            return repo.save(workingDate);
        }
        return workingDate;
    }

    private void updateCell(Row row, int cellIndex, CellStyle cellStyle, Boolean value) {
        if (value) {
            row.createCell(cellIndex).setCellValue("Onsite");
        } else {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue("Offsite");
            cell.setCellStyle(cellStyle);
        }
    }

}
