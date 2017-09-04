package com.dreamgear.majiangserver.net.buffer;

public enum JoyCharset {
	  UTF_8("UTF-8"), 

	  UTF_16("UTF-16"), 
	  UTF_16BE("UTF-16BE"), 
	  UTF_16LE("UTF-16LE"), 

	  UTF_32("UTF-32"), 
	  UTF_32BE("UTF-32BE"), 
	  UTF_32LE("UTF-32LE"), 

	  ISO_8859_1("ISO-8859-1"), 
	  US_ASCII("US-ASCII"), 

	  GBK("GBK"), 
	  GB2312("GB2312");

	  private String charsetName;

	  private JoyCharset(String charsetName)
	  {
	    this.charsetName = charsetName;
	  }

	  public String charsetName()
	  {
	    return this.charsetName;
	  }
}
