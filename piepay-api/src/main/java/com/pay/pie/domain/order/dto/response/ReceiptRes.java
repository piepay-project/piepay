package com.pay.pie.domain.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.pay.pie.domain.orderMenu.entity.OrderMenu;
import com.pay.pie.domain.participant.dto.ParticipantInfoDto;
import com.pay.pie.domain.payInstead.dto.PayInsteadDto;
import com.pay.pie.domain.store.dto.StoreInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ReceiptRes {

	private Long orderId;
	private StoreInfoDto storeInfo;
	private Long totalAmount;
	private List<ParticipantInfoDto> completedPaymentParticipantDtoList;
	private List<PayInsteadDto> payInsteadDtoList;
	private List<OrderMenu> orderManus;
	private LocalDateTime createdAt;

}