package com.shopme.admin.Util;

import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class UserCsvExporter extends AbstractExporter {


    public void export(List<User>listUser, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv",".csv");


    ICsvBeanWriter csvWritter=new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader={"User ID","E mail","First Name","Last Name","Roles","Enabled"};
        String[] fieladMapping={"id","email","firstName","lastName","roles","enabled"};
        
        csvWritter.writeHeader(csvHeader);
        for (User user : listUser) {
            csvWritter.write(user,fieladMapping);
        }
        csvWritter.close();

    }
}
