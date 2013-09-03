package org.libex.test.xmlunit;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableList;

public class IgnoreNamedElementsDifferenceListener implements DifferenceListener {

	private final ImmutableList<DifferenceListener> delegates;

	public IgnoreNamedElementsDifferenceListener(DifferenceListener... delegates) {
		this(ImmutableList.copyOf(delegates));
	}

	public IgnoreNamedElementsDifferenceListener(ImmutableList<DifferenceListener> delegates) {
		super();
		this.delegates = delegates;
	}

	@Override
	public int differenceFound(Difference diff) {
		for (DifferenceListener delegate : delegates) {
			int returnValue = delegate.differenceFound(diff);
			if (returnValue != DifferenceListener.RETURN_ACCEPT_DIFFERENCE) {
				return returnValue;
			}
		}

		return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	}

	@Override
	public void skippedComparison(Node arg0, Node arg1) {
	}
}
