package com.example.shopman.validation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NegativePageException extends Exception{

	private static final long serialVersionUID = 1L;
	private int page;
	public int getPage() {
		return page;
	}
}
