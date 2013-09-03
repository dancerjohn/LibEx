package org.libex.test.gettersetter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.Document;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TestClass1 {

	int intValue = 0;
	Integer integerValue = 0;
	Integer nullableIntegerValue = null;
	String stringValue = null;
	String trimmedStringValue = null;
	Document document = null;

	public String intValue() {
		return "blah";
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int value) {
		System.out.println("setIntValue with " + value);
		this.intValue = value;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public TestClass1 withIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
		return this;
	}

	public Integer getNullableIntegerValue() {
		return nullableIntegerValue;
	}

	public void setNullableIntegerValue(@Nullable Integer nullableIntegerValue) {
		System.out.println("setNullableIntegerValue with " + nullableIntegerValue);
		this.nullableIntegerValue = nullableIntegerValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(@Nullable String stringValue) {
		this.stringValue = stringValue;
	}

	public String getTrimmedStringValue() {
		return trimmedStringValue;
	}

	public void setTrimmedStringValue(String trimmedStringValue) {
		this.trimmedStringValue = trimmedStringValue.trim();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(@Nullable Document document) {
		System.out.println("setDocument with " + document);
		this.document = document;
	}

}
