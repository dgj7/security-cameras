package com.dg.securitycams.pattern.transform;

public interface Transformer<I, O> {
    O transform(final I input);
}
