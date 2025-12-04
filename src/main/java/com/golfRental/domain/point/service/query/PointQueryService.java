package com.golfRental.domain.point.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.point.dto.response.PointTransactionGetResponse;

public interface PointQueryService {

    SliceResponse<PointTransactionGetResponse> getMyTransactions(Long userId, int page, int size);
}
