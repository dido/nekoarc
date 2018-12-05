package com.stormwyrm.nekoarc.types;

import java.util.concurrent.SynchronousQueue;

public class Chan extends ArcObject {
    public final ArcObject TYPE = Symbol.intern("chan");
    private final SynchronousQueue<ArcObject> q;

    public Chan() {
        q = new SynchronousQueue<>();
    }

    public void send(ArcObject obj) {
        for (;;) {
            try {
                q.put(obj);
                return;
            } catch (InterruptedException e) { }
        }
    }

    public ArcObject recv() {
        for (;;) {
            try {
                return(q.take());
            } catch (InterruptedException e) { }
        }
    }

    @Override
    public ArcObject type() {
        return(TYPE);
    }
}
