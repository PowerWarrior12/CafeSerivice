package com.simbirsoft.services.impl;

import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.services.reports.ReportGenerator;
import com.simbirsoft.services.reports.ReportType;
import com.simbirsoft.services.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.simbirsoft.constants.ErrorMessages.REPORT_EXCEPTION;
import static com.simbirsoft.constants.ErrorMessages.REPORT_HANDLER_EXCEPTION;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportServiceImpl implements ReportService {
    private final List<ReportGenerator<?>> reportGeneratorList;
    @Override
    public byte[] generateReport(ReportType reportType) {
        for (int index = 0; index <= reportGeneratorList.size(); index++) {
            if (reportGeneratorList.get(index).match(reportType)) {
                try {
                    return reportGeneratorList.get(index).generate();
                } catch (IOException e) {
                    throw new ApiRequestException(REPORT_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        throw new ApiRequestException(REPORT_HANDLER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
