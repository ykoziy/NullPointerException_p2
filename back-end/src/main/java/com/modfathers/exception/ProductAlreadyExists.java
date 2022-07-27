package com.modfathers.exception;

public class ProductAlreadyExists extends RuntimeException
{

	public ProductAlreadyExists()
	{
		super();
	}

	public ProductAlreadyExists(String message)
	{
		super(message);
	}

}
