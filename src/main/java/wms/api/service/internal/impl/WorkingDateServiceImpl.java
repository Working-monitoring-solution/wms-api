package wms.api.service.internal.impl;

import org.graalvm.compiler.lir.LIRInstruction;
import org.springframework.stereotype.Service;
import wms.api.common.request.SendLocationRequest;
import wms.api.dao.entity.User;
import wms.api.dao.entity.WorkingDate;
import wms.api.dao.repo.WorkingDateRepository;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.WorkingDateService;

import javax.servlet.http.HttpServletRequest;

@Service
public class WorkingDateServiceImpl extends BaseServiceImpl<WorkingDateRepository, WorkingDate, Long> implements WorkingDateService {

    @Override
    public WorkingDate handleLocation(SendLocationRequest sendLocationRequest, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);

        return null;
    }
}
