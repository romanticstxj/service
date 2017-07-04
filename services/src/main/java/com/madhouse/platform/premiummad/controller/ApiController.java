package com.madhouse.platform.premiummad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/api")
public class ApiController {

	@RequestMapping("")
	public void getInterfaceFile(HttpServletResponse response) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("api.txt");
		InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator", "\n");
		String line ;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append(lineSeparator);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String result = sb.toString();
		out.print(new String(result.getBytes("UTF-8")));
		out.flush();
		out.close();
		reader.close();
	}
}
