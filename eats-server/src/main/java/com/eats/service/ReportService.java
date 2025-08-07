package com.eats.service;

import com.eats.vo.OrderReportVO;
import com.eats.vo.SalesTop10ReportVO;
import com.eats.vo.TurnoverReportVO;
import com.eats.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {

    /**
     * 指定された時間範囲内の売上データを統�?
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 指定された時間範囲内のユーザーデータを統�?
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 指定された時間範囲内の注文データを統�?
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 指定された時間範囲内の売上ランキングトッ�?0を統�?
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 運営データレポートをエクスポート
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
