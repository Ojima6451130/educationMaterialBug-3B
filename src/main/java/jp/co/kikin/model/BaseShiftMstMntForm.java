package jp.co.kikin.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class BaseShiftMstMntForm {

    private static final long serialVersionUID = 1483629197030517493L;

    private String employeeId;
    private String employeeName;

    private List<BaseShiftMstMntBean> baseShiftMstMntBeanList;
    private Map<String, String> shiftCmbMap;
    private List<BaseShiftPatternBean> baseShiftPatternBeanList;

    private String shiftName;
    private String symbol;
    private String timeZone;
    private String breakTime;

    private String shiftIdOnMonday;
    private String shiftIdOnTuesday;
    private String shiftIdOnWednesday;
    private String shiftIdOnThursday;
    private String shiftIdOnFriday;
    private String shiftIdOnSaturday;
    private String shiftIdOnSunday;

    private String createrEmployeeId;
    private Date creationDatetime;
    private String updaterEmployeeId;
    private Date updateDatetime;
}
