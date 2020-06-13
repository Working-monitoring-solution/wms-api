package wms.api.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import wms.api.common.response.ManagerResponse;
import wms.api.common.response.RequestResponse;
import wms.api.common.response.RequestResponseMobile;
import wms.api.dao.entity.Request;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkingDateTransform {
    @Autowired
    UserTransform userTransform;

    public RequestResponse toRequestResponse(Request request) {
        ManagerResponse managerResponse = null;
        if (!ObjectUtils.isEmpty(request.getManager())) {
            managerResponse = ManagerResponse.builder()
                    .id(request.getManager().getId())
                    .email(request.getManager().getEmail())
                    .name(request.getManager().getName())
                    .build();
        }
        return RequestResponse.builder()
                .id(request.getId())
                .date(request.getDate())
                .reason(request.getReason())
                .manager(managerResponse)
                .status(request.getStatus())
                .user(userTransform.toUserResponse(request.getUser()))
                .build();
    }

    public RequestResponseMobile toRequestResponseMobile(Request request) {
        return RequestResponseMobile.builder()
                .date(request.getDate())
                .reason(request.getReason())
                .status(request.getStatus())
                .build();
    }

    public List<RequestResponse> toListRequestResponse(List<Request> requestList) {
        List<RequestResponse> responseList = new ArrayList<>();
        for (Request request : requestList) {
            responseList.add(toRequestResponse(request));
        }
        return responseList;
    }

    public List<RequestResponseMobile> toListRequestResponseMobile(List<Request> requestList) {
        List<RequestResponseMobile> responseList = new ArrayList<>();
        for (Request request : requestList) {
            responseList.add(toRequestResponseMobile(request));
        }
        return responseList;
    }

    public Page<RequestResponse> toPageRequestResponse(Page<Request> requestPage) {
        Page<RequestResponse> responsePage = new PageImpl<>(toListRequestResponse(requestPage.getContent()),
                requestPage.getPageable(),
                requestPage.getTotalElements());
        return responsePage;
    }
}
