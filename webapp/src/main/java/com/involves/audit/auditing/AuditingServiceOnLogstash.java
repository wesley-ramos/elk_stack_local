package com.involves.audit.auditing;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.involves.audit.configuration.AppConfiguration;

@Service
public class AuditingServiceOnLogstash implements AuditingService {

	private DatagramSocket socket;
	private ObjectMapper objectMapper;

	@Autowired
	public AuditingServiceOnLogstash(DatagramSocket socket, ObjectMapper objectMapper) {
		this.socket = socket;
		this.objectMapper = objectMapper;
	}

	@Override
	public void audit(String functionality, Auditing auditing) {
		
		try {
			InetAddress address = InetAddress.getByName("localhost");
			
			Map<String, Object> data = auditing.getAuditing();
			data.put("functionality", functionality);
			data.put("application", AppConfiguration.APPLICATION_NAME);
			
			byte[] message = objectMapper.writeValueAsString(data).getBytes();

			DatagramPacket packet = new DatagramPacket(message, message.length, address, 8090);
			socket.send(packet);

		} catch (Exception ex) {
			System.out.println("Chegou aqui:"+ex.getMessage());
		}
	}
}
