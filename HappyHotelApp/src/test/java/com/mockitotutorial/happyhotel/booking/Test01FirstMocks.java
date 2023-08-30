
package com.mockitotutorial.happyhotel.booking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.internal.matchers.Any;

class Test01FirstMocks {

	private BookingService bookingService;
	private PaymentService paymentServiceMock;
	private RoomService roomServiceMock;
	private BookingDAO bookingDAOMock;
	private MailSender mailSenderMock;

	@BeforeEach
	void setUp() {
		this.paymentServiceMock = mock(PaymentService.class);
		this.roomServiceMock = mock(RoomService.class);
		;
		this.bookingDAOMock = mock(BookingDAO.class);
		;
		this.mailSenderMock = mock(MailSender.class);
		;

		this.bookingService = new BookingService(paymentServiceMock, roomServiceMock, bookingDAOMock, mailSenderMock);
	}

	@Test
	void testCalculatePrice() {
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 9, 1), LocalDate.of(2021, 9, 05), 2,
				false);
		double expected = 50.0 * 4 * 2;
		double calculatePrice = bookingService.calculatePrice(bookingRequest);
		assertEquals(expected, calculatePrice);
		;
	}

	@Test
	void testCountAvailablePlaces() {
		Room room = new Room("2", 2);
		Room room2 = new Room("3", 4);
		List<Room> values = new ArrayList<>();
		values.add(room);
		values.add(room2);
		when(roomServiceMock.getAvailableRooms()).thenReturn(values).thenReturn(Collections.singletonList(new Room("room 4", 7)));
		/*
		 * assertEquals(6, bookingService.getAvailablePlaceCount()); assertEquals(7,
		 * bookingService.getAvailablePlaceCount());
		 */
	assertAll(()->assertEquals(6, bookingService.getAvailablePlaceCount()),
			()->assertEquals(7, bookingService.getAvailablePlaceCount())			);
	}
	
	@Test
	void testMakeBookingThrowExceptionWhenNoRoom() {
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 9, 1), LocalDate.of(2021, 9, 05), 2,
				false);
		when(this.roomServiceMock.findAvailableRoomId(any()))
		.thenThrow(BusinessException.class);
		
		Executable executable=()->bookingService.makeBooking(bookingRequest);
		assertThrows(BusinessException.class, executable); 

		
	}
	
	@Test
	void testMakeBookingThrowExceptionWhenMailSent() {
		BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2021, 9, 1), LocalDate.of(2021, 9, 05), 2,
				false);
	doThrow(BusinessException.class).when(this.mailSenderMock).sendBookingConfirmation((any()));
//	doNothing().when(this.mailSenderMock).sendBookingConfirmation((any()));
	
		Executable executable=()->bookingService.makeBooking(bookingRequest);
		assertThrows(BusinessException.class, executable); 

		
	}
	}


