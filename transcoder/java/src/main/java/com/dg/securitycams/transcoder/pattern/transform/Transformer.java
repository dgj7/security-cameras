package com.dg.securitycams.transcoder.pattern.transform;

public interface Transformer<I, O> {
    O transform(final I input);
}
