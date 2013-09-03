package org.libex.test.xmlunit;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableSet;

public class IgnoreNamedAttributesDifferenceListener implements DifferenceListener {

	private final ImmutableSet<String> attributeNamesToIgnore;

	public IgnoreNamedAttributesDifferenceListener(String... attributeNamesToIgnore) {
		this(ImmutableSet.copyOf(attributeNamesToIgnore));
	}

	public IgnoreNamedAttributesDifferenceListener(ImmutableSet<String> attributeNamesToIgnore) {
		super();
		this.attributeNamesToIgnore = attributeNamesToIgnore;
	}

	@Override
	public int differenceFound(Difference diff) {
		if (diff.getId() == DifferenceConstants.ATTR_VALUE_ID) {
			String attributeName = diff.getControlNodeDetail().getNode().getNodeName();
			if (attributeNamesToIgnore.contains(attributeName)) {
				return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
			}
		}
		return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	}

	@Override
	public void skippedComparison(Node arg0, Node arg1) {
	}
}
