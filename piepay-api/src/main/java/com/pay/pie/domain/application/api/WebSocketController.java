package com.pay.pie.domain.application.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.pay.pie.domain.application.PayAgreeService;
import com.pay.pie.domain.application.PayInsteadService;
import com.pay.pie.domain.application.dto.AgreeDto;
import com.pay.pie.domain.application.dto.InsteadDto;
import com.pay.pie.domain.application.dto.request.AgreeReq;
import com.pay.pie.domain.application.dto.request.InsteadAgreeReq;
import com.pay.pie.domain.application.dto.request.InsteadRequestReq;
import com.pay.pie.domain.participant.application.ParticipantService;
import com.pay.pie.global.security.dto.SecurityUserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
// @RequestMapping
@RequiredArgsConstructor
public class WebSocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

	private final SimpMessageSendingOperations simpleMessageSendingOperations;
	private final ParticipantService participantService;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, AgreeDto> redisTemplateAgreeData;
	private final RedisTemplate<String, InsteadDto> redisTemplateInsteadData;
	private final PayAgreeService payAgreeService;
	private final SimpMessagingTemplate messagingTemplate;
	private final PayInsteadService payInsteadService;

	// 새로운 사용자가 웹 소켓을 연결할 때 실행됨
	// @EventListener은 한개의 매개변수만 가질 수 있다.
	// @EventListener
	// public void handleWebSocketConnectListener(SessionConnectEvent event) {
	// 	log.info("연결!");
	// 	StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
	// 	String sessionId = headerAccesor.getSessionId();
	// 	LOGGER.info("Received a new web socket connection : " + sessionId);
	// }
	//
	// 사용자가 웹 소켓 연결을 끊으면 실행됨
	// @EventListener
	// public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
	// 	StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
	// 	String sessionId = headerAccesor.getSessionId();
	//
	// 	LOGGER.info("sessionId Disconnected : " + sessionId);
	// }

	/**
	 * 연결 확인용
	 * @param message
	 * @return
	 */
	@MessageMapping("/channel")
	public String sendMessage(String message) {
		log.info("Received message: {}", message);
		simpleMessageSendingOperations.convertAndSend("/sub", "socket connection completed.");
		return message;
	}

	// @GetMapping("/pay/{payId}")
	// public ResponseEntity<List<AgreeDto>> loadAgreeData(@PathVariable String payId) {
	// 	List<AgreeDto> agreeRedis = redisTemplateAgreeData.opsForList().range(payId, 0, 99);
	//
	// 	return ResponseEntity.ok(agreeRedis);
	// }

	/**
	 * 방 입장 시 초기 정보 조회
	 * @param payId
	 * @param headerAccessor 초기 정보 리스트
	 */
	@MessageMapping("/InitialData/{payId}")
	public void checkInitialData(@DestinationVariable String payId, SimpMessageHeaderAccessor headerAccessor) {
		// 클라이언트가 방에 입장하여 초기 데이터를 확인할 때 호출됩니다.
		// 해당 방의 초기 정보를 조회하여 클라이언트에게 전송합니다.
		String sessionId = headerAccessor.getSessionId();
		List<Object> initialData = new ArrayList<>();
		Map<Object, Object> agreeData = redisTemplate.opsForHash().entries("payId:" + payId + ":agree");
		Map<Object, Object> insteadData = redisTemplate.opsForHash().entries("payId:" + payId + ":instead");

		Map<String, Object> formattedData = new HashMap<>();
		// agreeData 변환 후 저장
		if (agreeData != null && !agreeData.isEmpty()) {
			Map<String, Boolean> formattedAgreeData = new HashMap<>();
			for (Map.Entry<Object, Object> entry : agreeData.entrySet()) {
				formattedAgreeData.put(entry.getKey().toString(), Boolean.parseBoolean(entry.getValue().toString()));
			}
			formattedData.put("agreeData", formattedAgreeData);
		}

		// insteadData 변환 후 저장
		if (insteadData != null && !insteadData.isEmpty()) {
			Map<String, Object> formattedInsteadData = new HashMap<>();
			for (Map.Entry<Object, Object> entry : insteadData.entrySet()) {
				formattedInsteadData.put(entry.getKey().toString(), entry.getValue());
			}
			formattedData.put("insteadData", formattedInsteadData);
		}

		// if (agreeData != null) {
		// 	initialData.add(agreeData);
		// }
		// if (insteadData != null) {
		// 	initialData.add(insteadData);
		// }
		log.info("받아올 데이터: {}", formattedData);
		messagingTemplate.convertAndSendToUser(sessionId, "/sub/initialData/" + payId, formattedData);
	}

	/**
	 * 결제 동의
	 * @param agreeReq
	 */
	@MessageMapping("/agree")
	public void respondToAgreement(AgreeReq agreeReq) {
		log.info("payId:{}", agreeReq.getPayId());
		AgreeDto agreeDto = payAgreeService.respondToAgreement(agreeReq);

		// Send message to relevant participants via WebSocket
		messagingTemplate.convertAndSend("/sub/" + agreeReq.getPayId(), agreeDto);
		// messagingTemplate.convertAndSend("/sub/" + agreeReq.getPayId(), "power of love");
		log.info("동의 성공");
	}

	/**
	 * 대신내주기 요청
	 * @param insteadReq
	 */
	@MessageMapping("/instead-req")
	public void requestPayInstead(
		@AuthenticationPrincipal SecurityUserDto securityUserDto, InsteadRequestReq insteadReq) {
		Long borrowerId = securityUserDto.getMemberId();
		InsteadDto insteadDto = payInsteadService.requestPayInstead(insteadReq.getPayId(), borrowerId);

		// Send message to relevant participants via WebSocket
		messagingTemplate.convertAndSend("/sub/" + insteadDto.getPayId(), insteadDto);
	}

	/**
	 * 대신내주기 승낙
	 * @param insteadReq
	 */
	@MessageMapping("/instead-res")
	public void respondToPayInstead(
		@AuthenticationPrincipal SecurityUserDto securityUserDto, InsteadAgreeReq insteadReq) {
		Long lenderId = securityUserDto.getMemberId();
		InsteadDto insteadDto = payInsteadService.respondToPayInstead(
			insteadReq.getPayId(), insteadReq.getBorrowerId(), lenderId);

		// Send message to relevant participants via WebSocket
		messagingTemplate.convertAndSend("/sub/" + insteadDto.getPayId(), insteadDto);

	}
}
