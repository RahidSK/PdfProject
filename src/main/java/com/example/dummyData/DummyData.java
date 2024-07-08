package com.example.dummyData;


import com.example.model.BusinessEntity;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<BusinessEntity> setValues() {
        List<BusinessEntity> list = new ArrayList<>();

        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setDate("March 4, 2024");
        businessEntity.setSummaryPeriod("February 1, 2024 - February 29, 2024");
        businessEntity.setMerchantNumber("4445017773879");
        businessEntity.setBusinessName("ROBERT HALF INTERNATIONAL INC.");
        businessEntity.setMailingAddress("2613 Camino Ramon PO BOX 743295 San Ramon, CA 94583");
        businessEntity.setSubMIDNumber(000000101);

        list.add(businessEntity);

        return list;
    }
}

//private String date;
//private String Summary_Period;
//private Long Merchant_Number;
//private String Business_Name;
//private String Mailing_Address;
//private Long Sub_MID_Number;
