package edu.kh.project.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {
	
	private static final String serviceKey = "%2BX2omxDJaPOq2SDxwAUvVauLEbNGdoWdvZG46pplpaLTn1ElMZpx610qq2rzkBPkt2xfw8Is8gWW0TJG%2Bi12Rw%3D%3D";
	
	@RequestMapping(value="busInfo", produces="application/json; charset=utf-8")
	@ResponseBody
	public String busInfo() throws Exception{
		
		String url = "https://apis.data.go.kr/1741000/TsunamiShelter3/getTsunamiShelter1List?serviceKey=%2BX2omxDJaPOq2SDxwAUvVauLEbNGdoWdvZG46pplpaLTn1ElMZpx610qq2rzkBPkt2xfw8Is8gWW0TJG%2Bi12Rw%3D%3D"
				+ "&pageNo=1"
				+ "&numOfRows=2&type=json";
		
		URL requestUrl = new URL(url);

		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();

		urlConnection.setRequestMethod("GET");

		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		String responseData = "";
		String line; 

		while((line = br.readLine()) != null) {
				responseData += line;
				}
		
		br.close();

		urlConnection.disconnect(); 

		System.out.println("responseData::" + responseData);
				
		return responseData;

	}
	
}
