package com.shopme.admin.Util;

import com.shopme.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contenType,String extension) throws IOException {

        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStap=dateFormatter.format(new Date());
        String fileName="users_"+ timeStap+ extension;
        response.setContentType(contenType);

        String headerKey="Content-Disposition";
        String headerValue="attachment; filename="+fileName;
        response.setHeader(headerKey,headerValue);
    }
}
