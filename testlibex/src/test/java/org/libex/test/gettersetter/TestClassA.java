package org.libex.test.gettersetter;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;
import lombok.Setter;

import org.joda.time.DateTime;
import org.w3c.dom.Document;

@ParametersAreNonnullByDefault
@ThreadSafe
@Getter
@Setter
public class TestClassA {

	@Nullable
	private Boolean booleanWrapper = false;
	private boolean booleanPrimitive = false;
	private boolean trueBooleanPrimitive = true;

	@Nullable
	private Byte byteWrapper = null;
	private byte byteValue = 1;

	@Nullable
	private Short shortWrapper = null;
	private short shortValue = 1;

	@Nullable
	private Integer integerWrapper = null;
	private int intValue = 1;

	@Nonnull
	private Long longWrapper = 1L;
	private long longValue = 1L;

	@Nullable
	private Float floatWrapper = null;
	private float floatValue = 1;

	@Nullable
	private Double doubleWrapper = null;
	private double doubleValue = 1;

	private final String finalString = "blah";
	private String stringValue;

	private Object objectValue = null;
	@Nullable
	private Date dateValue = null;
	@Nullable
	private DateTime dateTimeValue = null;

	private Document document = null;
}
