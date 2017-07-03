package com.stelinno.uddi.search;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
@WebServlet(name = "uddiSearchVersion", description = "Search: Index a new document", urlPatterns = "/uddi-search/version")
public class VersionServlet extends HttpServlet {

	@Autowired
	private String version;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println(version);
	}
}
