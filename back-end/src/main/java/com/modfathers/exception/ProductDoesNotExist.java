package com.modfathers.exception;

public class ProductDoesNotExist extends RuntimeException
{

	public ProductDoesNotExist()
	{
		super();
		
	}

	public ProductDoesNotExist(String message)
	{
		super(message);
		
	}

}
