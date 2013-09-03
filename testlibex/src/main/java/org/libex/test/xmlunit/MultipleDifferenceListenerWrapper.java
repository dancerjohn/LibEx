package org.libex.test.xmlunit;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableSet;

public class MultipleDifferenceListenerWrapper implements DifferenceListener {

	private final ImmutableSet<String> elementLocalNamesToIgnore;

	public MultipleDifferenceListenerWrapper(String... elementLocalNamesToIgnore) {
		this(ImmutableSet.copyOf(elementLocalNamesToIgnore));
	}

	public MultipleDifferenceListenerWrapper(ImmutableSet<String> elementLocalNamesToIgnore) {
		super();
		this.elementLocalNamesToIgnore = elementLocalNamesToIgnore;
	}

	@Override
	public int differenceFound(Difference diff) {
		if (diff.getId() == DifferenceConstants.TEXT_VALUE_ID) {
			String elementName = diff.getControlNodeDetail().getNode().getParentNode().getLocalName();
			if (elementLocalNamesToIgnore.contains(elementName)) {
				return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
			}
		}
		return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	}

	@Override
	public void skippedComparison(Node arg0, Node arg1) {
	}
}
