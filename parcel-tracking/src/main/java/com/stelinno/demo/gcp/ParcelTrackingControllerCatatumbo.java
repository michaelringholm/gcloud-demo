package com.stelinno.demo.gcp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(name = "parceltracking2", value = "/create" )
public class ParcelTrackingControllerCatatumbo extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    out.println("{ creating new parcel with Catatumbo.... }");
    new ParcelServiceCatatumbo().insertNew();
  }
}

