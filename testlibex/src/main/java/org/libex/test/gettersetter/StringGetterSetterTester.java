package org.libex.test.gettersetter;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class StringGetterSetterTester extends BaseGetterSetterTester<String> {

	private static final List<String> values = newArrayList(
			null, "", "a", "1", "short", "ComBine1234$&#@&%*$*", "  ", " asfsd ", "     11222       \r\n",
			"asdfjaslkdfjas 2joiJjlkjlkfjdlksjJFKJSDLKFJSDFLKJ$#$%*#(%*#)(%*#)(*)#%(* FJSDKLFJSLDKJLK SDGJ " +
					"lksdjflksdjf slkdjf sjwioeruewoiruoweiurowiejrw230948230948209348029348sdjflksjdf s sljkdflskjdf" +
					"lskdfjer8029384092384209843092384092fjlkje lwkejf wlkej welrj ijsoeijoijfwiejowijOIU%)%()%#(*#%)(*#)(%*");

	public StringGetterSetterTester() {
		super(String.class, values);
	}
}
